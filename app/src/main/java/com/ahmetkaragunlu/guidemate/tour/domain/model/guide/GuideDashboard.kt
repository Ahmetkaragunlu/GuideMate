package com.ahmetkaragunlu.guidemate.tour.domain.model.guide

import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier

data class GuideDashboard(
    val activeSessionCount: Long,
    val pendingReviewCount: Long,
    val completedSessionCount: Long,
    val totalParticipantCount: Long,
    val averageRating: Double,
    val reviewCount: Long,
    val level: GuideLevelTier,
    val currentMonthEarningsMinor: Long,
    val currencyCode: String,
)
