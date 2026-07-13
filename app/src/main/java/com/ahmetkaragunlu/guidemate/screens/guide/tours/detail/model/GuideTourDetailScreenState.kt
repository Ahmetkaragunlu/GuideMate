package com.ahmetkaragunlu.guidemate.screens.guide.tours.detail.model

import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailUiState

data class GuideTourDetailScreenState(
    val detail: TourDetailUiState,
    val mode: TourDetailMode,
    val action: GuideTourDetailActionUiState,
)
