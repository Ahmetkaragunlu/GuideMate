package com.ahmetkaragunlu.guidemate.screens.tourist.tours.model

import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailUiState
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.catalog.TourBookingAvailability
import com.ahmetkaragunlu.guidemate.screens.tourist.reviews.model.TourReviewAvailability
import com.ahmetkaragunlu.guidemate.screens.tourist.reviews.model.TourReviewFormUiState

data class TouristTourDetailScreenState(
    val detail: TourDetailUiState,
    val bookingAvailability: TourBookingAvailability,
    val reservationId: String?,
    val reviewAvailability: TourReviewAvailability,
    val reviewForm: TourReviewFormUiState,
)
