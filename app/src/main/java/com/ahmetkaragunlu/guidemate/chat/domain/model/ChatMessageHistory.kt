package com.ahmetkaragunlu.guidemate.chat.domain.model

data class ChatMessageHistory(
    val messages: List<ChatMessage> = emptyList(),
    val nextCursor: String? = null,
    val hasMore: Boolean = false,
)
