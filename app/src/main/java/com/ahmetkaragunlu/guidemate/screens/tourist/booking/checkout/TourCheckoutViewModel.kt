package com.ahmetkaragunlu.guidemate.screens.tourist.booking.checkout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.TouristPaymentDestination
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.mapper.toTourDetailUiState
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.catalog.resolveBookingAvailability
import com.ahmetkaragunlu.guidemate.screens.common.tours.store.TourCatalogStore
import com.ahmetkaragunlu.guidemate.screens.common.tours.store.refreshAtSessionTransitions
import com.ahmetkaragunlu.guidemate.screens.tourist.booking.checkout.model.TourCheckoutUiState
import com.ahmetkaragunlu.guidemate.screens.tourist.booking.model.checkoutErrorResId
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.store.TouristFinanceStore
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.store.TouristPaymentStore
import com.ahmetkaragunlu.guidemate.screens.tourist.reservations.store.TouristReservationStore
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class TourCheckoutViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val tourCatalogStore: TourCatalogStore,
        private val financeStore: TouristFinanceStore,
        private val paymentStore: TouristPaymentStore,
        private val reservationStore: TouristReservationStore,
    ) : ViewModel() {
        private val sessionId =
            savedStateHandle.toRoute<TouristPaymentDestination.Checkout>().sessionId
        private val actionState = MutableStateFlow(TourCheckoutUiState(sessionId = sessionId))

        val uiState: StateFlow<TourCheckoutUiState> =
            combine(
                tourCatalogStore.state.refreshAtSessionTransitions(),
                financeStore.state,
                actionState,
            ) {
                    catalog,
                    finance,
                    action,
                ->
                val now = Instant.now()
                val tourWithSession = catalog.findBySessionId(sessionId)
                val detail = tourWithSession?.toTourDetailUiState(now)
                val availableCapacity =
                    tourWithSession?.session?.let { (it.capacity - it.bookedCount).coerceAtLeast(0) }
                        ?: 0

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
                    walletBalanceMinor = finance.balanceMinor,
                    savedCards = finance.savedCards,
                    selectedCardId =
                        action.selectedCardId
                            ?.takeIf { selectedId ->
                                finance.savedCards.any { it.cardId == selectedId }
                            }
                            ?: finance.defaultCard?.cardId,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = initialState(),
            )

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
                it.copy(
                    selectedMethod = method,
                    validationErrorResId = null,
                )
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
                it.copy(
                    termsAccepted = isAccepted,
                    validationErrorResId = null,
                )
            }
        }

        fun createPaymentAttempt(): String? {
            val state = uiState.value
            val tourWithSession = tourCatalogStore.state.value.findBySessionId(sessionId)
            val hasReservation =
                reservationStore.reservations.value.any { reservation ->
                    reservation.tourSessionId == sessionId
                }
            val bookingAvailability =
                tourWithSession.resolveBookingAvailability(hasReservation = hasReservation)
            val currentAvailableCapacity =
                tourWithSession?.session?.let { session ->
                    (session.capacity - session.bookedCount).coerceAtLeast(0)
                } ?: 0

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

        private fun initialState(): TourCheckoutUiState {
            val tourWithSession = tourCatalogStore.state.value.findBySessionId(sessionId)
            val detail = tourWithSession?.toTourDetailUiState(Instant.now())
            val finance = financeStore.state.value
            return TourCheckoutUiState(
                sessionId = sessionId,
                tourTitle = detail?.title.orEmpty(),
                date = detail?.date.orEmpty(),
                location = detail?.location.orEmpty(),
                unitPriceMinor = detail?.priceMinor ?: 0,
                availableCapacity =
                    tourWithSession?.session?.let {
                        (it.capacity - it.bookedCount).coerceAtLeast(0)
                    } ?: 0,
                walletBalanceMinor = finance.balanceMinor,
                savedCards = finance.savedCards,
                selectedCardId = finance.defaultCard?.cardId,
            )
        }
    }
