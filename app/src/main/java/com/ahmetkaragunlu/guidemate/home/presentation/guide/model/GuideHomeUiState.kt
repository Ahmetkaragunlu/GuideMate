package com.ahmetkaragunlu.guidemate.home.presentation.guide.model

data class GuideHomeUiState(
    val pendingCount: Int = 0,
    val activeCount: Int = 0,
    val dashboardStats: List<GuideStatistic> = emptyList(),
)
