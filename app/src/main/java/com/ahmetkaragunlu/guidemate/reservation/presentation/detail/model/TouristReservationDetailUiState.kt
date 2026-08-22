package com.ahmetkaragunlu.guidemate.reservation.presentation.detail.model

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.review.presentation.model.TourReviewFormUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState

data class TouristReservationDetailUiState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val detail: TourDetailUiState? = null,
    val reservationStatus: TouristReservationStatus? = null,
    val canSubmitReview: Boolean = false,
    val reviewForm: TourReviewFormUiState = TourReviewFormUiState(),
    @param:StringRes val noticeResId: Int? = null,
)
