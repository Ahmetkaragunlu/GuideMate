package com.ahmetkaragunlu.guidemate.tour.presentation.tourist.detail.model

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourBookingAvailability

data class TouristTourDetailScreenState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val detail: TourDetailUiState? = null,
    val bookingAvailability: TourBookingAvailability = TourBookingAvailability.UNAVAILABLE,
)
