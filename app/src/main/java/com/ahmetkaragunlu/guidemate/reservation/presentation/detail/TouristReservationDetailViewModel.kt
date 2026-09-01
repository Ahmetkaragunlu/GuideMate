package com.ahmetkaragunlu.guidemate.reservation.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.navigation.tourist.TouristDestination
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRefundEligibility
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.reservation.domain.repository.ReservationRepository
import com.ahmetkaragunlu.guidemate.reservation.presentation.detail.model.TouristReservationDetailUiState
import com.ahmetkaragunlu.guidemate.reservation.presentation.mapper.toTourDetailUiState
import com.ahmetkaragunlu.guidemate.review.domain.repository.ReviewRepository
import com.ahmetkaragunlu.guidemate.review.domain.model.ReviewSubmissionInput
import com.ahmetkaragunlu.guidemate.review.presentation.model.TourReviewFormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TouristReservationDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val reservationRepository: ReservationRepository,
        private val reviewRepository: ReviewRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val reservationId =
            savedStateHandle.toRoute<TouristDestination.ReservationDetail>().reservationId
        private val _uiState = MutableStateFlow(TouristReservationDetailUiState())
        val uiState: StateFlow<TouristReservationDetailUiState> = _uiState.asStateFlow()
        private var loadJob: Job? = null
        private var submitJob: Job? = null
        private var currentReservation: TouristReservation? = null

        init {
            refresh()
        }

        fun refresh() {
            if (loadJob?.isActive == true) return
            loadJob =
                viewModelScope.launch {
                    _uiState.value =
                        TouristReservationDetailUiState(loadState = ContentLoadState.LOADING)
                    when (val result = reservationRepository.getReservation(reservationId)) {
                        is DataResult.Success -> showReservation(result.data)
                        is DataResult.Error -> {
                            _uiState.value =
                                TouristReservationDetailUiState(loadState = ContentLoadState.ERROR)
                        }
                    }
                }
        }

        fun showReviewForm() {
            val reservation = currentReservation ?: return
            if (!reservation.canSubmitReview()) return
            _uiState.update {
                it.copy(reviewForm = TourReviewFormUiState(isVisible = true))
            }
        }

        fun dismissReviewForm() {
            if (_uiState.value.reviewForm.isSubmitting) return
            _uiState.update { it.copy(reviewForm = TourReviewFormUiState()) }
        }

        fun updateReviewRating(rating: Int) {
            if (_uiState.value.reviewForm.isSubmitting) return
            _uiState.update {
                it.copy(
                    reviewForm =
                        it.reviewForm.copy(
                            rating = rating.coerceIn(MIN_REVIEW_RATING, MAX_REVIEW_RATING),
                            errorMessage = null,
                        ),
                )
            }
        }

        fun updateReviewComment(comment: String) {
            if (_uiState.value.reviewForm.isSubmitting) return
            _uiState.update {
                it.copy(
                    reviewForm =
                        it.reviewForm.copy(
                            comment = comment.take(MAX_REVIEW_COMMENT_LENGTH),
                            errorMessage = null,
                        ),
                )
            }
        }

        fun submitReview() {
            val reservation = currentReservation ?: return
            val form = _uiState.value.reviewForm
            if (!reservation.canSubmitReview() || form.isSubmitting) return
            if (form.rating !in MIN_REVIEW_RATING..MAX_REVIEW_RATING) {
                _uiState.update {
                    it.copy(
                        reviewForm =
                            it.reviewForm.copy(
                                errorMessage =
                                    resourceProvider.getString(R.string.tour_review_rating_required),
                            ),
                    )
                }
                return
            }

            submitJob?.cancel()
            submitJob =
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            reviewForm =
                                it.reviewForm.copy(
                                    isSubmitting = true,
                                    errorMessage = null,
                                ),
                        )
                    }
                    when (
                        val result =
                            reviewRepository.submitReview(
                                reservationId = reservationId,
                                input =
                                    ReviewSubmissionInput(
                                        rating = form.rating,
                                        comment = form.comment,
                                    ),
                            )
                    ) {
                        is DataResult.Success -> {
                            val updatedReservation = reservation.copy(review = result.data)
                            currentReservation = updatedReservation
                            showReservation(
                                reservation = updatedReservation,
                                reviewForm = TourReviewFormUiState(showSuccessDialog = true),
                            )
                            refreshReservationAfterReview()
                        }
                        is DataResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    reviewForm =
                                        it.reviewForm.copy(
                                            isSubmitting = false,
                                            errorMessage = result.error.toMessage(resourceProvider),
                                        ),
                                )
                            }
                        }
                    }
                }
        }

        fun dismissReviewSuccessDialog() {
            _uiState.update {
                it.copy(reviewForm = it.reviewForm.copy(showSuccessDialog = false))
            }
        }

        private suspend fun showReservation(
            reservation: TouristReservation,
            reviewForm: TourReviewFormUiState = _uiState.value.reviewForm,
        ) {
            currentReservation = reservation
            val reviews =
                when (
                    val result =
                        reviewRepository.getTourReviews(
                            tourId = reservation.snapshot.tourId,
                            page = 0,
                            size = REVIEW_PREVIEW_SIZE,
                        )
                ) {
                    is DataResult.Success -> result.data
                    is DataResult.Error -> null
                }
            _uiState.value =
                TouristReservationDetailUiState(
                    loadState = ContentLoadState.CONTENT,
                    detail =
                        reservation.toTourDetailUiState(
                            publicReviews = reviews?.items.orEmpty(),
                        ),
                    reservationStatus = reservation.status,
                    canSubmitReview = reservation.canSubmitReview(),
                    reviewForm = reviewForm,
                    noticeResId = reservation.noticeResId(),
                )
        }

        private suspend fun refreshReservationAfterReview() {
            when (val result = reservationRepository.getReservation(reservationId)) {
                is DataResult.Success ->
                    showReservation(
                        reservation = result.data,
                        reviewForm = _uiState.value.reviewForm,
                    )
                is DataResult.Error -> Unit
            }
        }

        private fun TouristReservation.canSubmitReview(): Boolean =
            status == TouristReservationStatus.COMPLETED && review == null

        private fun TouristReservation.noticeResId(): Int? =
            when {
                status == TouristReservationStatus.COMPLETED && review != null ->
                    R.string.tour_review_submitted_notice
                status == TouristReservationStatus.CANCELLED &&
                    refundEligibility == ReservationRefundEligibility.FULL_REFUND ->
                    R.string.reservation_detail_full_refund_notice
                status == TouristReservationStatus.CANCELLED &&
                    refundEligibility == ReservationRefundEligibility.NO_REFUND ->
                    R.string.reservation_detail_no_refund_notice
                else -> null
            }

        private companion object {
            const val REVIEW_PREVIEW_SIZE = 20
            const val MIN_REVIEW_RATING = 1
            const val MAX_REVIEW_RATING = 5
            const val MAX_REVIEW_COMMENT_LENGTH = 2_000
        }
    }
