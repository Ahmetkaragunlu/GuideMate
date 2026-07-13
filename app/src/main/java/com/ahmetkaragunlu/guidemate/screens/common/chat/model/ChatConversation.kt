package com.ahmetkaragunlu.guidemate.screens.common.chat.model

import java.time.Instant

data class ChatConversation(
    val chatId: String,
    val guide: ChatParticipant,
    val tourist: ChatParticipant,
    val createdAt: Instant,
    val messages: List<ChatMessage>,
    val unreadCounts: Map<String, Int> = emptyMap(),
) {
    val lastMessage: ChatMessage?
        get() = messages.maxByOrNull(ChatMessage::sentAt)

    val lastActivityAt: Instant
        get() = lastMessage?.sentAt ?: createdAt

    fun containsUser(userId: String): Boolean = guide.userId == userId || tourist.userId == userId

    fun otherParticipant(userId: String): ChatParticipant? =
        when (userId) {
            guide.userId -> tourist
            tourist.userId -> guide
            else -> null
        }

    fun unreadCount(userId: String): Int = unreadCounts[userId] ?: 0
}
