package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail.model

import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState

data class GuideTourDetailScreenState(
    val detail: TourDetailUiState,
    val mode: TourDetailMode,
    val action: GuideTourDetailActionUiState,
)
