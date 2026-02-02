package com.ahmetkaragunlu.guidemate.screens.tourist.chat.model

data class ChatUiModel(
    val id: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val avatarResId: Int,
    val unreadCount: Int = 0
)