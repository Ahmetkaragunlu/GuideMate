package com.ahmetkaragunlu.guidemate.profile.domain.model

import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier

data class GuideSearchResult(
    val guideId: Long,
    val displayName: String,
    val specialtyTitle: String,
    val avatar: MediaReference?,
    val languageCodes: List<String>,
    val completedSessionCount: Long,
    val totalParticipantCount: Long,
    val averageRating: Double,
    val reviewCount: Long,
    val level: GuideLevelTier,
)
