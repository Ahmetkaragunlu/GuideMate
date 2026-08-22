package com.ahmetkaragunlu.guidemate.profile.domain.model

import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.profile.domain.model.performance.GuidePerformanceSummary

data class GuideProfile(
    val guideId: Long,
    val firstName: String,
    val lastName: String,
    val displayName: String,
    val specialtyTitle: String,
    val biography: String,
    val languageCodes: List<String>,
    val avatar: MediaReference?,
    val performance: GuidePerformanceSummary,
)
