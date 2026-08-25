package com.ahmetkaragunlu.guidemate.reservation.presentation.checkout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.TouristPaymentDestination
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import com.ahmetkaragunlu.guidemate.payment.presentation.locale.currentCheckoutLocale
import com.ahmetkaragunlu.guidemate.payment.presentation.model.PaymentLaunch
import com.ahmetkaragunlu.guidemate.reservation.presentation.checkout.model.checkoutErrorResId
import com.ahmetkaragunlu.guidemate.reservation.presentation.checkout.model.TourCheckoutUiState
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.resolveBookingAvailability
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.mapper.toTourDetailUiState
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.util.Currency
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TourCheckoutViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        private val tourRepository: TourDiscoveryRepository,
        private val walletRepository: WalletRepository,
        private val paymentRepository: PaymentRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val sessionId =
            savedStateHandle.toRoute<TouristPaymentDestination.Checkout>().sessionId
        private val currentTour = MutableStateFlow<TourWithSession?>(null)
        private val actionState = MutableStateFlow(TourCheckoutUiState(sessionId = sessionId))
        private var loadJob: Job? = null
        private var paymentJob: Job? = null
        private var checkoutIdempotencyKey: String?
            get() = savedStateHandle[CHECKOUT_IDEMPOTENCY_KEY]
            set(value) {
                savedStateHandle[CHECKOUT_IDEMPOTENCY_KEY] = value
            }

        val uiState: StateFlow<TourCheckoutUiState> =
            combine(currentTour, actionState) { tourWithSession, action ->
                val detail = tourWithSession?.toTourDetailUiState(Instant.now())
                val availableCapacity = tourWithSession?.session?.availableCapacity ?: 0

                action.copy(
                    tourTitle = detail?.title.orEmpty(),
                    date = detail?.date.orEmpty(),
                    location = detail?.location.orEmpty(),
                    unitPriceMinor = detail?.priceMinor ?: 0,
                    participantCount =
                        action.participantCount.coerceIn(
                            minimumValue = 1,
                            maximumValue = availableCapacity.coerceAtLeast(1),
                        ),
                    availableCapacity = availableCapacity,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = actionState.value,
            )

        init {
            refreshTour()
        }

        fun refreshTour() {
            if (loadJob?.isActive == true) return
            loadJob =
                viewModelScope.launch {
                    actionState.update { it.copy(loadState = ContentLoadState.LOADING) }
                    val tour = tourRepository.getSession(sessionId)
                    val wallet = walletRepository.getWallet()
                    val currencies = paymentRepository.getCheckoutCurrencies()
                    if (
                        tour !is DataResult.Success ||
                            wallet !is DataResult.Success ||
                            currencies !is DataResult.Success
                    ) {
                        currentTour.value = null
                        actionState.update { it.copy(loadState = ContentLoadState.ERROR) }
                        return@launch
                    }

                    currentTour.value = tour.data
                    actionState.update { current ->
                        current.copy(
                            loadState = ContentLoadState.CONTENT,
                            walletBalanceMinor = wallet.data.balanceMinor,
                            walletCurrencyCode = wallet.data.currencyCode,
                            chargeCurrencies = currencies.data.chargeCurrencies,
                            selectedChargeCurrencyCode =
                                current.selectedChargeCurrencyCode
                                    ?.takeIf { code ->
                                        currencies.data.chargeCurrencies.any {
                                            it.currencyCode == code
                                        }
                                    } ?: currencies.data.preferredCurrencyCode(),
                        )
                    }
                }
        }

        fun decreaseParticipantCount() {
            actionState.update { current ->
                current.copy(
                    participantCount = (current.participantCount - 1).coerceAtLeast(1),
                    quote = null,
                    validationErrorResId = null,
                    paymentActionError = null,
                )
            }
            checkoutIdempotencyKey = null
        }

        fun increaseParticipantCount() {
            actionState.update { current ->
                current.copy(
                    participantCount =
                        (current.participantCount + 1).coerceAtMost(
                            uiState.value.availableCapacity.coerceAtLeast(1),
                        ),
                    quote = null,
                    validationErrorResId = null,
                    paymentActionError = null,
                )
            }
            checkoutIdempotencyKey = null
        }

        fun onPaymentMethodSelected(method: PaymentMethod) {
            actionState.update {
                it.copy(
                    selectedMethod = method,
                    quote = null,
                    validationErrorResId = null,
                    paymentActionError = null,
                )
            }
            checkoutIdempotencyKey = null
        }

        fun onChargeCurrencySelected(currencyCode: String) {
            if (currencyCode == actionState.value.selectedChargeCurrencyCode) return
            actionState.update {
                it.copy(
                    selectedChargeCurrencyCode = currencyCode,
                    quote = null,
                    paymentActionError = null,
                )
            }
            checkoutIdempotencyKey = null
        }

        fun onTermsAcceptedChange(isAccepted: Boolean) {
            actionState.update {
                it.copy(termsAccepted = isAccepted, validationErrorResId = null)
            }
        }

        fun continueCheckout() {
            if (paymentJob?.isActive == true) return
            val state = uiState.value
            val errorResId = validate(state)
            if (errorResId != null) {
                actionState.update { it.copy(validationErrorResId = errorResId) }
                return
            }

            paymentJob =
                viewModelScope.launch {
                    actionState.update {
                        it.copy(isPaymentActionInProgress = true, paymentActionError = null)
                    }
                    if (state.selectedMethod == PaymentMethod.HOSTED_CARD) {
                        val quote = state.quote
                        if (quote == null || quote.isExpired(Instant.now())) {
                            requestQuote(state)
                            return@launch
                        }
                    }
                    initializePayment(uiState.value)
                }
        }

        fun onPaymentNavigationHandled() {
            actionState.update { it.copy(paymentLaunch = null) }
        }

        private suspend fun requestQuote(state: TourCheckoutUiState) {
            val currencyCode = checkNotNull(state.selectedChargeCurrencyCode)
            when (
                val result =
                    paymentRepository.quoteTour(
                        sessionId = sessionId,
                        participantCount = state.participantCount,
                        chargeCurrencyCode = currencyCode,
                    )
            ) {
                is DataResult.Success ->
                    actionState.update {
                        it.copy(
                            quote = result.data,
                            isPaymentActionInProgress = false,
                        )
                    }
                is DataResult.Error -> showPaymentError(result)
            }
        }

        private suspend fun initializePayment(state: TourCheckoutUiState) {
            val idempotencyKey =
                checkoutIdempotencyKey ?: UUID.randomUUID().toString().also {
                    checkoutIdempotencyKey = it
                }
            when (
                val result =
                    paymentRepository.checkoutTour(
                        sessionId = sessionId,
                        participantCount = state.participantCount,
                        method = state.selectedMethod,
                        quoteId = state.quote?.id,
                        locale = currentCheckoutLocale(),
                        idempotencyKey = idempotencyKey,
                    )
            ) {
                is DataResult.Success -> {
                    checkoutIdempotencyKey = null
                    actionState.update {
                        it.copy(
                            isPaymentActionInProgress = false,
                            paymentLaunch =
                                PaymentLaunch(
                                    paymentId = result.data.id,
                                    requiresHostedCheckout =
                                        result.data.method == PaymentMethod.HOSTED_CARD,
                                ),
                        )
                    }
                }
                is DataResult.Error -> showPaymentError(result)
            }
        }

        private fun validate(state: TourCheckoutUiState): Int? {
            val tourWithSession = currentTour.value
            val bookingAvailability =
                tourWithSession.resolveBookingAvailability(hasReservation = false)
            val currentAvailableCapacity = tourWithSession?.session?.availableCapacity ?: 0
            return when {
                !bookingAvailability.isBookable -> bookingAvailability.checkoutErrorResId
                currentAvailableCapacity < state.participantCount ->
                    R.string.checkout_error_capacity_changed
                !state.termsAccepted -> R.string.checkout_error_terms_required
                state.selectedMethod == PaymentMethod.WALLET &&
                    state.walletBalanceMinor < state.totalMinor ->
                    R.string.checkout_error_insufficient_balance
                state.selectedMethod == PaymentMethod.HOSTED_CARD &&
                    state.selectedChargeCurrencyCode == null ->
                    R.string.payment_currency_required
                else -> null
            }
        }

        private fun showPaymentError(error: DataResult.Error) {
            actionState.update {
                it.copy(
                    isPaymentActionInProgress = false,
                    paymentActionError = error.error.toMessage(resourceProvider),
                )
            }
        }

        private fun CheckoutCurrencies.preferredCurrencyCode(): String? {
            val deviceCurrencyCode =
                runCatching { Currency.getInstance(Locale.getDefault()).currencyCode }.getOrNull()
            return chargeCurrencies.firstOrNull { it.currencyCode == deviceCurrencyCode }?.currencyCode
                ?: chargeCurrencies.firstOrNull { it.currencyCode == baseCurrencyCode }?.currencyCode
                ?: chargeCurrencies.firstOrNull()?.currencyCode
        }

        private companion object {
            const val CHECKOUT_IDEMPOTENCY_KEY = "checkout_idempotency_key"
        }
    }
