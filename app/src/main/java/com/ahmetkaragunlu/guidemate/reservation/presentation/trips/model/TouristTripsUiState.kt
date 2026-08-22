package com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class TouristTripsUiState(
    val selectedTab: TripTab = TripTab.UPCOMING,
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val trips: List<TripUiModel> = emptyList(),
    val canLoadMore: Boolean = false,
    val isLoadingMore: Boolean = false,
    val appendFailed: Boolean = false,
    val cancellingReservationId: String? = null,
    val cancellationFeedback: ReservationCancellationFeedback? = null,
)

data class ReservationCancellationFeedback(
    val isSuccess: Boolean,
    val message: String,
)
