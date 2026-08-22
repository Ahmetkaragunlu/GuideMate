package com.ahmetkaragunlu.guidemate.reservation.presentation.checkout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.TouristPaymentDestination
import com.ahmetkaragunlu.guidemate.payment.data.mock.TouristPaymentStore
import com.ahmetkaragunlu.guidemate.payment.domain.repository.SavedPaymentMethodRepository
import com.ahmetkaragunlu.guidemate.payment.presentation.mapper.toUiModel
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.reservation.presentation.checkout.model.checkoutErrorResId
import com.ahmetkaragunlu.guidemate.reservation.presentation.model.TourCheckoutUiState
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.resolveBookingAvailability
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.mapper.toTourDetailUiState
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
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
        savedStateHandle: SavedStateHandle,
        private val tourRepository: TourDiscoveryRepository,
        private val walletRepository: WalletRepository,
        private val savedPaymentMethodRepository: SavedPaymentMethodRepository,
        private val paymentStore: TouristPaymentStore,
    ) : ViewModel() {
        private val sessionId =
            savedStateHandle.toRoute<TouristPaymentDestination.Checkout>().sessionId
        private val currentTour = MutableStateFlow<TourWithSession?>(null)
        private val actionState = MutableStateFlow(TourCheckoutUiState(sessionId = sessionId))
        private var loadJob: Job? = null

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
            viewModelScope.launch {
                savedPaymentMethodRepository.paymentMethodChanges.collect {
                    refreshPaymentContext()
                }
            }
        }

        fun refreshTour() {
            if (loadJob?.isActive == true) return
            loadJob =
                viewModelScope.launch {
                    actionState.update { it.copy(loadState = ContentLoadState.LOADING) }
                    val tour = tourRepository.getSession(sessionId)
                    val wallet = walletRepository.getWallet()
                    val cards = savedPaymentMethodRepository.getSavedPaymentMethods()
                    if (
                        tour is DataResult.Error ||
                            wallet is DataResult.Error ||
                            cards is DataResult.Error
                    ) {
                        currentTour.value = null
                        actionState.update { it.copy(loadState = ContentLoadState.ERROR) }
                        return@launch
                    }
                    currentTour.value = (tour as DataResult.Success).data
                    val walletData = (wallet as DataResult.Success).data
                    val cardItems =
                        (cards as DataResult.Success).data
                            .map { it.toUiModel() }
                            .sortedByDescending { it.isDefault }
                    actionState.update { current ->
                        current.copy(
                            loadState = ContentLoadState.CONTENT,
                            walletBalanceMinor = walletData.balanceMinor,
                            walletCurrencyCode = walletData.currencyCode,
                            savedCards = cardItems,
                            selectedCardId =
                                current.selectedCardId?.takeIf { selectedId ->
                                    cardItems.any { it.cardId == selectedId }
                                } ?: cardItems.firstOrNull { it.isDefault }?.cardId
                                    ?: cardItems.firstOrNull()?.cardId,
                        )
                    }
                }
        }

        private suspend fun refreshPaymentContext() {
            val wallet = walletRepository.getWallet()
            val cards = savedPaymentMethodRepository.getSavedPaymentMethods()
            if (wallet !is DataResult.Success || cards !is DataResult.Success) return
            val cardItems = cards.data.map { it.toUiModel() }.sortedByDescending { it.isDefault }
            actionState.update { current ->
                current.copy(
                    walletBalanceMinor = wallet.data.balanceMinor,
                    walletCurrencyCode = wallet.data.currencyCode,
                    savedCards = cardItems,
                    selectedCardId =
                        current.selectedCardId?.takeIf { selectedId ->
                            cardItems.any { it.cardId == selectedId }
                        } ?: cardItems.firstOrNull { it.isDefault }?.cardId
                            ?: cardItems.firstOrNull()?.cardId,
                )
            }
        }

        fun decreaseParticipantCount() {
            actionState.update { current ->
                current.copy(
                    participantCount = (current.participantCount - 1).coerceAtLeast(1),
                    validationErrorResId = null,
                )
            }
        }

        fun increaseParticipantCount() {
            actionState.update { current ->
                current.copy(
                    participantCount =
                        (current.participantCount + 1).coerceAtMost(
                            uiState.value.availableCapacity.coerceAtLeast(1),
                        ),
                    validationErrorResId = null,
                )
            }
        }

        fun onPaymentMethodSelected(method: PaymentMethod) {
            actionState.update {
                it.copy(selectedMethod = method, validationErrorResId = null)
            }
        }

        fun onCardSelected(cardId: String) {
            actionState.update {
                it.copy(
                    selectedCardId = cardId,
                    selectedMethod = PaymentMethod.SAVED_CARD,
                    validationErrorResId = null,
                )
            }
        }

        fun onTermsAcceptedChange(isAccepted: Boolean) {
            actionState.update {
                it.copy(termsAccepted = isAccepted, validationErrorResId = null)
            }
        }

        fun createPaymentAttempt(): String? {
            val state = uiState.value
            val tourWithSession = currentTour.value
            val bookingAvailability =
                tourWithSession.resolveBookingAvailability(hasReservation = false)
            val currentAvailableCapacity = tourWithSession?.session?.availableCapacity ?: 0

            val errorResId =
                when {
                    !bookingAvailability.isBookable -> bookingAvailability.checkoutErrorResId
                    currentAvailableCapacity < state.participantCount ->
                        R.string.checkout_error_capacity_changed
                    !state.termsAccepted -> R.string.checkout_error_terms_required
                    state.selectedMethod == PaymentMethod.WALLET &&
                        state.walletBalanceMinor < state.totalMinor ->
                        R.string.checkout_error_insufficient_balance
                    state.selectedMethod == PaymentMethod.SAVED_CARD &&
                        state.selectedCardId == null ->
                        R.string.checkout_error_card_required
                    else -> null
                }

            if (errorResId != null) {
                actionState.update { it.copy(validationErrorResId = errorResId) }
                return null
            }

            return paymentStore.createAttempt(
                purpose = PaymentPurpose.TOUR_BOOKING,
                amountMinor = state.totalMinor,
                method = state.selectedMethod,
                tourSessionId = sessionId,
                participantCount = state.participantCount,
                savedCardId = state.selectedCardId.takeIf {
                    state.selectedMethod == PaymentMethod.SAVED_CARD
                },
            )
        }
    }
