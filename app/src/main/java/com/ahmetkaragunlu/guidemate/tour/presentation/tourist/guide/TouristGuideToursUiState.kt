package com.ahmetkaragunlu.guidemate.tour.presentation.tourist.guide

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.tour.presentation.model.TourSearchResultUiModel

data class TouristGuideToursUiState(
    val tours: List<TourSearchResultUiModel> = emptyList(),
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val isLoadingMore: Boolean = false,
    val appendFailed: Boolean = false,
    val canLoadMore: Boolean = false,
)
