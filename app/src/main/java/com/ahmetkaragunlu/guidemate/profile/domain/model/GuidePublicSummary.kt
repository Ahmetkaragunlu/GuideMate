package com.ahmetkaragunlu.guidemate.profile.domain.model

import androidx.annotation.DrawableRes

internal const val MOCK_CURRENT_GUIDE_ID = "guide-current"

data class GuidePublicSummary(
    val id: String,
    val displayName: String,
    @param:DrawableRes val profileImageResId: Int,
    val profileImageUrl: String? = null,
)
