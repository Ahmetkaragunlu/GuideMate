package com.ahmetkaragunlu.guidemate.chat.domain.model

import java.time.Instant

data class ChatConversation(
    val chatId: String,
    val guide: ChatParticipant,
    val tourist: ChatParticipant,
    val lastMessage: ChatMessage?,
    val unreadCount: Int,
    val createdAt: Instant,
    val lastActivityAt: Instant,
) {
    fun containsUser(userId: Long): Boolean = guide.userId == userId || tourist.userId == userId

    fun otherParticipant(userId: Long): ChatParticipant? =
        when (userId) {
            guide.userId -> tourist
            tourist.userId -> guide
            else -> null
        }
}
