package com.ahmetkaragunlu.guidemate.profile.domain.model

data class GuidePublicSummary(
    val id: String,
    val displayName: String,
    val profileImageUrl: String? = null,
)
