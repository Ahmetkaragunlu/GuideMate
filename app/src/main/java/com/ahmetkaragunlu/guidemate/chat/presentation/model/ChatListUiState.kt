package com.ahmetkaragunlu.guidemate.chat.presentation.model

data class ChatListUiState(
    val chats: List<ChatUiModel> = emptyList(),
    val totalUnreadCount: Int = 0,
)
