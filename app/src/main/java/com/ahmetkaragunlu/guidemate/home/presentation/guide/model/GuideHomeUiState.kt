package com.ahmetkaragunlu.guidemate.home.presentation.guide.model

import com.ahmetkaragunlu.guidemate.common.ui.formatting.PLATFORM_CURRENCY_CODE
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class GuideHomeUiState(
    val pendingCount: Long = 0,
    val activeCount: Long = 0,
    val dashboardStats: List<GuideStatistic> = emptyList(),
    val currentMonthEarningsMinor: Long = 0,
    val currencyCode: String = PLATFORM_CURRENCY_CODE,
    val dashboardLoadState: ContentLoadState = ContentLoadState.LOADING,
)
