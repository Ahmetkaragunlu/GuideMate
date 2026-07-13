package com.ahmetkaragunlu.guidemate.screens.common.chat.model

import androidx.annotation.DrawableRes

data class ChatUiModel(
    val chatId: String,
    val remoteUserId: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    @param:DrawableRes val avatarResId: Int,
    val avatarUrl: String? = null,
    val unreadCount: Int = 0,
)
