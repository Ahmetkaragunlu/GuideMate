package com.ahmetkaragunlu.guidemate.home.presentation.guide.model

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class GuideHomeUiState(
    val pendingCount: Long = 0,
    val activeCount: Long = 0,
    val dashboardStats: List<GuideStatistic> = emptyList(),
    val currentMonthEarningsMinor: Long = 0,
    val currencyCode: String = "USD",
    val dashboardLoadState: ContentLoadState = ContentLoadState.LOADING,
)
