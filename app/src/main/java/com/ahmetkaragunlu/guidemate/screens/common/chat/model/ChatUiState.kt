package com.ahmetkaragunlu.guidemate.screens.common.chat.model

data class ChatListUiState(
    val chats: List<ChatUiModel> = emptyList(),
    val totalUnreadCount: Int = 0,
)

data class ChatDetailUiState(
    val messages: List<MessageUiModel> = emptyList(),
    val inputText: String = "",
)
