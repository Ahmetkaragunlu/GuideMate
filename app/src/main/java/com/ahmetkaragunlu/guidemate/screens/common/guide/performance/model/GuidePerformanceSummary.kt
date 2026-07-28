package com.ahmetkaragunlu.guidemate.screens.common.guide.performance.model

import com.ahmetkaragunlu.guidemate.screens.common.guide.level.model.GuideLevelTier

data class GuidePerformanceSummary(
    val completedSessionCount: Int,
    val totalParticipantCount: Int,
    val averageRating: Double,
    val reviewCount: Int,
    val level: GuideLevelTier,
)
