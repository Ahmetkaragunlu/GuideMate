package com.ahmetkaragunlu.guidemate.profile.domain.model

import androidx.annotation.DrawableRes

data class GuidePublicSummary(
    val id: String,
    val displayName: String,
    @param:DrawableRes val profileImageResId: Int,
    val profileImageUrl: String? = null,
)
