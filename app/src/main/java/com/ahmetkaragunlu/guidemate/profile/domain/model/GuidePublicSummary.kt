package com.ahmetkaragunlu.guidemate.profile.domain.model

data class GuidePublicSummary(
    val id: Long,
    val displayName: String,
    val profileImageUrl: String? = null,
)
