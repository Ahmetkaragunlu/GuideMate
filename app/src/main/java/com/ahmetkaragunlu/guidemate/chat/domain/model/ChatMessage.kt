package com.ahmetkaragunlu.guidemate.chat.domain.model

import java.time.Instant

data class ChatMessage(
    val messageId: String,
    val chatId: String,
    val senderId: Long,
    val clientMessageId: String,
    val text: String,
    val sentAt: Instant,
    val deliveryStatus: ChatMessageDeliveryStatus,
)

enum class ChatMessageDeliveryStatus {
    PENDING,
    SENT,
    FAILED,
}
