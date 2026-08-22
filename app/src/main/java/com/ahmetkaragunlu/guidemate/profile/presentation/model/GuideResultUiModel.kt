package com.ahmetkaragunlu.guidemate.profile.presentation.model

import androidx.annotation.DrawableRes
import com.ahmetkaragunlu.guidemate.R

data class GuideResultUiModel(
    val guideId: Long,
    val displayName: String,
    val specialtyTitle: String,
    val avatarUrl: String?,
    @param:DrawableRes val fallbackAvatarResId: Int = R.drawable.unnamed,
    val averageRating: Double,
)
