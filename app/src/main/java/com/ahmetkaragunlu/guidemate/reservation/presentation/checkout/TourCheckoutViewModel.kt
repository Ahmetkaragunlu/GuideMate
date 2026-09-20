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
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import com.ahmetkaragunlu.guidemate.payment.presentation.currency.preferredChargeCurrencyCode
import com.ahmetkaragunlu.guidemate.payment.presentation.locale.currentCheckoutLocale
import com.ahmetkaragunlu.guidemate.payment.presentation.model.PaymentLaunch
import com.ahmetkaragunlu.guidemate.reservation.presentation.checkout.model.checkoutErrorResId
import com.ahmetkaragunlu.guidemate.reservation.presentation.checkout.model.TourCheckoutSummaryUiState
import com.ahmetkaragunlu.guidemate.reservation.presentation.checkout.model.TourCheckoutUiState
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.resolveBookingAvailability
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.mapper.toTourDetailUiState
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        private var currentTour: TourWithSession? = null
        private val mutableUiState = MutableStateFlow(TourCheckoutUiState())
        private var loadJob: Job? = null
        private var paymentJob: Job? = null
        private var checkoutIdempotencyKey: String?
            get() = savedStateHandle[CHECKOUT_IDEMPOTENCY_KEY]
            set(value) {
                savedStateHandle[CHECKOUT_IDEMPOTENCY_KEY] = value
            }

        val uiState: StateFlow<TourCheckoutUiState> = mutableUiState.asStateFlow()

        init {
            refreshTour()
        }

        fun refreshTour() {
            if (loadJob?.isActive == true) return
            loadJob =
                viewModelScope.launch {
                    mutableUiState.update { it.copy(loadState = ContentLoadState.LOADING) }
                    val tour = tourRepository.getSession(sessionId)
                    val wallet = walletRepository.getWallet()
                    val currencies = paymentRepository.getCheckoutCurrencies()
                    if (
                        tour !is DataResult.Success ||
                            wallet !is DataResult.Success ||
                            currencies !is DataResult.Success
                    ) {
                        currentTour = null
                        mutableUiState.update {
                            it.copy(
                                loadState = ContentLoadState.ERROR,
                                summary = TourCheckoutSummaryUiState(),
                                participantCount = 1,
                            )
                        }
                        return@launch
                    }

                    currentTour = tour.data
                    val detail = tour.data.toTourDetailUiState()
                    val availableCapacity = tour.data.session.availableCapacity
                    mutableUiState.update { current ->
                        current.copy(
                            loadState = ContentLoadState.CONTENT,
                            summary =
                                current.summary.copy(
                                    tourTitle = detail.tour.title,
                                    date = detail.session.date,
                                    location = detail.tour.location,
                                    unitPriceMinor = detail.session.priceMinor,
                                    availableCapacity = availableCapacity,
                                ),
                            participantCount =
                                current.participantCount.coerceIn(
                                    minimumValue = 1,
                                    maximumValue = availableCapacity.coerceAtLeast(1),
                                ),
                            wallet =
                                current.wallet.copy(
                                    balanceMinor = wallet.data.balanceMinor,
                                    currencyCode = wallet.data.currencyCode,
                                ),
                            payment =
                                current.payment.copy(
                                    chargeCurrencies = currencies.data.chargeCurrencies,
                                    selectedChargeCurrencyCode =
                                        current.payment.selectedChargeCurrencyCode
                                    ?.takeIf { code ->
                                        currencies.data.chargeCurrencies.any {
                                            it.currencyCode == code
                                        }
                                    } ?: currencies.data.preferredChargeCurrencyCode(),
                                ),
                        )
                    }
                }
        }

        fun decreaseParticipantCount() {
            mutableUiState.update { current ->
                current.copy(
                    participantCount = (current.participantCount - 1).coerceAtLeast(1),
                    payment = current.payment.copy(quote = null),
                    submission =
                        current.submission.copy(
                            validationErrorResId = null,
                            paymentActionError = null,
                        ),
                )
            }
            checkoutIdempotencyKey = null
        }

        fun increaseParticipantCount() {
            mutableUiState.update { current ->
                current.copy(
                    participantCount =
                        (current.participantCount + 1).coerceAtMost(
                            uiState.value.summary.availableCapacity.coerceAtLeast(1),
                        ),
                    payment = current.payment.copy(quote = null),
                    submission =
                        current.submission.copy(
                            validationErrorResId = null,
                            paymentActionError = null,
                        ),
                )
            }
            checkoutIdempotencyKey = null
        }

        fun onPaymentMethodSelected(method: PaymentMethod) {
            mutableUiState.update {
                it.copy(
                    payment = it.payment.copy(selectedMethod = method, quote = null),
                    submission =
                        it.submission.copy(
                            validationErrorResId = null,
                            paymentActionError = null,
                        ),
                )
            }
            checkoutIdempotencyKey = null
        }

        fun onChargeCurrencySelected(currencyCode: String) {
            if (currencyCode == mutableUiState.value.payment.selectedChargeCurrencyCode) return
            mutableUiState.update {
                it.copy(
                    payment =
                        it.payment.copy(
                            selectedChargeCurrencyCode = currencyCode,
                            quote = null,
                        ),
                    submission = it.submission.copy(paymentActionError = null),
                )
            }
            checkoutIdempotencyKey = null
        }

        fun onTermsCheckboxClicked() {
            mutableUiState.update { current ->
                if (current.terms.isAccepted) {
                    current.copy(
                        terms = current.terms.copy(isAccepted = false),
                        submission = current.submission.copy(validationErrorResId = null),
                    )
                } else {
                    current.copy(
                        terms = current.terms.copy(isSheetVisible = true),
                        submission = current.submission.copy(validationErrorResId = null),
                    )
                }
            }
        }

        fun dismissTermsSheet() {
            mutableUiState.update {
                it.copy(terms = it.terms.copy(isSheetVisible = false))
            }
        }

        fun markTermsAsRead() {
            mutableUiState.update {
                it.copy(terms = it.terms.copy(hasBeenRead = true))
            }
        }

        fun acceptTerms() {
            if (!mutableUiState.value.terms.hasBeenRead) return
            mutableUiState.update {
                it.copy(
                    terms = it.terms.copy(isAccepted = true, isSheetVisible = false),
                    submission = it.submission.copy(validationErrorResId = null),
                )
            }
        }

        fun continueCheckout() {
            if (paymentJob?.isActive == true) return
            val state = uiState.value
            val errorResId = validate(state)
            if (errorResId != null) {
                mutableUiState.update {
                    it.copy(submission = it.submission.copy(validationErrorResId = errorResId))
                }
                return
            }

            paymentJob =
                viewModelScope.launch {
                    mutableUiState.update {
                        it.copy(
                            submission =
                                it.submission.copy(
                                    isPaymentActionInProgress = true,
                                    paymentActionError = null,
                                ),
                        )
                    }
                    if (state.payment.selectedMethod == PaymentMethod.HOSTED_CARD) {
                        val quote = state.payment.quote
                        if (quote == null || quote.isExpired(Instant.now())) {
                            requestQuote(state)
                            return@launch
                        }
                    }
                    initializePayment(uiState.value)
                }
        }

        fun onPaymentNavigationHandled() {
            mutableUiState.update {
                it.copy(submission = it.submission.copy(paymentLaunch = null))
            }
        }

        private suspend fun requestQuote(state: TourCheckoutUiState) {
            val currencyCode = checkNotNull(state.payment.selectedChargeCurrencyCode)
            when (
                val result =
                    paymentRepository.quoteTour(
                        sessionId = sessionId,
                        participantCount = state.participantCount,
                        chargeCurrencyCode = currencyCode,
                    )
            ) {
                is DataResult.Success ->
                    mutableUiState.update {
                        it.copy(
                            payment = it.payment.copy(quote = result.data),
                            submission =
                                it.submission.copy(isPaymentActionInProgress = false),
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
            val result =
                coroutineScope {
                    val minimumVerificationDuration =
                        if (state.payment.selectedMethod == PaymentMethod.WALLET) {
                            async { delay(MINIMUM_WALLET_VERIFICATION_MILLIS) }
                        } else {
                            null
                        }
                    val checkoutResult =
                        paymentRepository.checkoutTour(
                            sessionId = sessionId,
                            participantCount = state.participantCount,
                            method = state.payment.selectedMethod,
                            quoteId = state.payment.quote?.id,
                            locale = currentCheckoutLocale(),
                            idempotencyKey = idempotencyKey,
                        )
                    minimumVerificationDuration?.await()
                    checkoutResult
                }
            when (result) {
                is DataResult.Success -> {
                    checkoutIdempotencyKey = null
                    mutableUiState.update {
                        it.copy(
                            payment = it.payment.copy(quote = null),
                            submission =
                                it.submission.copy(
                                    isPaymentActionInProgress = false,
                                    paymentLaunch =
                                        PaymentLaunch(
                                            paymentId = result.data.id,
                                            requiresHostedCheckout =
                                                result.data.method == PaymentMethod.HOSTED_CARD,
                                        ),
                                ),
                        )
                    }
                }
                is DataResult.Error -> showPaymentError(result)
            }
        }

        private fun validate(state: TourCheckoutUiState): Int? {
            val tourWithSession = currentTour
            val bookingAvailability =
                tourWithSession.resolveBookingAvailability(hasReservation = false)
            val currentAvailableCapacity = tourWithSession?.session?.availableCapacity ?: 0
            return when {
                !bookingAvailability.isBookable -> bookingAvailability.checkoutErrorResId
                currentAvailableCapacity < state.participantCount ->
                    R.string.checkout_error_capacity_changed
                !state.terms.isAccepted -> R.string.checkout_error_terms_required
                state.payment.selectedMethod == PaymentMethod.WALLET &&
                    state.wallet.balanceMinor < state.totalMinor ->
                    R.string.checkout_error_insufficient_balance
                state.payment.selectedMethod == PaymentMethod.HOSTED_CARD &&
                    state.payment.selectedChargeCurrencyCode == null ->
                    R.string.payment_currency_required
                else -> null
            }
        }

        private fun showPaymentError(error: DataResult.Error) {
            mutableUiState.update {
                it.copy(
                    submission =
                        it.submission.copy(
                            isPaymentActionInProgress = false,
                            paymentActionError = error.error.toMessage(resourceProvider),
                        ),
                )
            }
        }

        private companion object {
            const val CHECKOUT_IDEMPOTENCY_KEY = "checkout_idempotency_key"
            const val MINIMUM_WALLET_VERIFICATION_MILLIS = 600L
        }
    }
