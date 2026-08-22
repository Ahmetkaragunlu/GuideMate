package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail.model

import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab

data class GuideTourDetailScreenState(
    val detail: TourDetailUiState? = null,
    val mode: TourDetailMode? = null,
    val action: GuideTourDetailActionUiState = GuideTourDetailActionUiState(),
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val userMessage: String? = null,
    val finishedTab: GuideTourTab? = null,
)
