package com.ahmetkaragunlu.guidemate.tour.presentation.tourist.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.tourist.TouristDestination
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.mapper.toTourDetailUiState
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourCatalogState
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.resolveBookingAvailability
import com.ahmetkaragunlu.guidemate.tour.data.mock.TourCatalogStore
import com.ahmetkaragunlu.guidemate.tour.data.mock.refreshAtSessionTransitions
import com.ahmetkaragunlu.guidemate.reservation.presentation.mapper.toTourDetailUiState
import com.ahmetkaragunlu.guidemate.reservation.domain.model.CreateTourReviewRequest
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation
import com.ahmetkaragunlu.guidemate.reservation.data.mock.TouristReservationStore
import com.ahmetkaragunlu.guidemate.review.presentation.model.TourReviewAvailability
import com.ahmetkaragunlu.guidemate.review.presentation.model.TourReviewFormUiState
import com.ahmetkaragunlu.guidemate.review.presentation.model.resolveTourReviewAvailability
import com.ahmetkaragunlu.guidemate.tour.presentation.tourist.detail.model.TouristTourDetailScreenState
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
class TouristTourDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        tourCatalogStore: TourCatalogStore,
        private val reservationStore: TouristReservationStore,
    ) : ViewModel() {
        private val sessionId = savedStateHandle.toRoute<TouristDestination.TourDetail>().sessionId
        private val reviewForm = MutableStateFlow(TourReviewFormUiState())

        val uiState: StateFlow<TouristTourDetailScreenState?> =
            combine(
                tourCatalogStore.state.refreshAtSessionTransitions(),
                reservationStore.reservations,
                reviewForm,
            ) {
                    catalog,
                    reservations,
                    form,
                ->
                    buildScreenState(
                        catalog = catalog,
                        reservations = reservations,
                        form = form,
                        now = Instant.now(),
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue =
                        buildScreenState(
                            catalog = tourCatalogStore.state.value,
                            reservations = reservationStore.reservations.value,
                            form = reviewForm.value,
                            now = Instant.now(),
                        ),
                )

        fun openReviewSheet() {
            if (uiState.value?.reviewAvailability != TourReviewAvailability.AVAILABLE) return
            reviewForm.update { it.copy(isVisible = true, errorResId = null) }
        }

        fun dismissReviewSheet() {
            reviewForm.update { it.copy(isVisible = false, errorResId = null) }
        }

        fun updateReviewRating(rating: Int) {
            reviewForm.update {
                it.copy(
                    rating = rating.coerceIn(1, 5),
                    errorResId = null,
                )
            }
        }

        fun updateReviewComment(comment: String) {
            reviewForm.update { it.copy(comment = comment) }
        }

        fun submitReview() {
            val screenState = uiState.value ?: return
            val reservationId = screenState.reservationId ?: return
            if (screenState.reviewAvailability != TourReviewAvailability.AVAILABLE) return

            val form = reviewForm.value
            if (form.rating !in 1..5) {
                reviewForm.update { it.copy(errorResId = R.string.tour_review_rating_required) }
                return
            }

            reviewForm.update { it.copy(isSubmitting = true, errorResId = null) }
            val submitted =
                reservationStore.submitReview(
                    CreateTourReviewRequest(
                        reservationId = reservationId,
                        rating = form.rating,
                        comment = form.comment,
                    ),
                )
            reviewForm.update {
                if (submitted) {
                    TourReviewFormUiState(showSuccessDialog = true)
                } else {
                    it.copy(
                        isSubmitting = false,
                        errorResId = R.string.tour_review_submission_failed,
                    )
                }
            }
        }

        fun dismissReviewSuccess() {
            reviewForm.update { it.copy(showSuccessDialog = false) }
        }

        private fun buildScreenState(
            catalog: TourCatalogState,
            reservations: List<TouristReservation>,
            form: TourReviewFormUiState,
            now: Instant,
        ): TouristTourDetailScreenState? {
            val currentTour = catalog.findBySessionId(sessionId)
            val reservation = reservations.firstOrNull { it.tourSessionId == sessionId }
            val bookingAvailability =
                currentTour.resolveBookingAvailability(
                    hasReservation = reservation != null,
                    now = now,
                )
            val detail =
                reservation?.toTourDetailUiState(currentTour = currentTour, now = now)
                    ?: currentTour?.toTourDetailUiState(now)
            return detail?.let {
                TouristTourDetailScreenState(
                    detail = it,
                    bookingAvailability = bookingAvailability,
                    reservationId = reservation?.id,
                    reviewAvailability =
                        resolveTourReviewAvailability(
                            reservation = reservation,
                            detailStatus = it.sessionStatus,
                        ),
                    reviewForm = form,
                )
            }
        }
    }
