package com.ahmetkaragunlu.guidemate.profile.domain.model.performance

import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier

data class GuidePerformanceSummary(
    val completedSessionCount: Long,
    val totalParticipantCount: Long,
    val averageRating: Double,
    val reviewCount: Long,
    val level: GuideLevelTier,
)
