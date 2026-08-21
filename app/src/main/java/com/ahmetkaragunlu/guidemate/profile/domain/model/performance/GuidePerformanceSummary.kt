package com.ahmetkaragunlu.guidemate.profile.domain.model.performance

import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier

data class GuidePerformanceSummary(
    val completedSessionCount: Int,
    val totalParticipantCount: Int,
    val averageRating: Double,
    val reviewCount: Int,
    val level: GuideLevelTier,
)
