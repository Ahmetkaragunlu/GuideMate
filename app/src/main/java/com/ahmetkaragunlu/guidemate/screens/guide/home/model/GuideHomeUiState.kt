package com.ahmetkaragunlu.guidemate.screens.guide.home.model

data class GuideHomeUiState(
    val monthlyEarnings: Double = 0.0,
    val pendingCount: Int = 0,
    val activeCount: Int = 0,
    val dashboardStats: List<GuideStatistic> = emptyList(),
    val recentActivities: List<RecentActivity> = emptyList(),
)
