package com.ahmetkaragunlu.guidemate.tour.presentation.tourist.detail.model

import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourBookingAvailability
import com.ahmetkaragunlu.guidemate.review.presentation.model.TourReviewAvailability
import com.ahmetkaragunlu.guidemate.review.presentation.model.TourReviewFormUiState

data class TouristTourDetailScreenState(
    val detail: TourDetailUiState,
    val bookingAvailability: TourBookingAvailability,
    val reservationId: String?,
    val reviewAvailability: TourReviewAvailability,
    val reviewForm: TourReviewFormUiState,
)
