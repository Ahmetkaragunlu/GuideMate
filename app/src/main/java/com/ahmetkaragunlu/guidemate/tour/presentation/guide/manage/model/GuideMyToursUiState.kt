package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class GuideMyToursUiState(
    val selectedTab: GuideTourTab = GuideTourTab.ACTIVE,
    val tours: List<GuideTourCardUiModel> = emptyList(),
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val isLoadingMore: Boolean = false,
    val appendFailed: Boolean = false,
    val canLoadMore: Boolean = false,
    val pendingSessionIds: Set<String> = emptySet(),
    val pendingArchiveTourIds: Set<String> = emptySet(),
    val userMessage: String? = null,
)
