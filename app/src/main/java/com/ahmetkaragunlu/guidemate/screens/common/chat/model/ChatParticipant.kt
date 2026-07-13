package com.ahmetkaragunlu.guidemate.screens.common.chat.model

import androidx.annotation.DrawableRes
import com.ahmetkaragunlu.guidemate.domain.model.UserRole

data class ChatParticipant(
    val userId: String,
    val displayName: String,
    val role: UserRole,
    val avatarUrl: String? = null,
    @param:DrawableRes val avatarResId: Int,
)
