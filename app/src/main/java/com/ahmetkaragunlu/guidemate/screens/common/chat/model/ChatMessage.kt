package com.ahmetkaragunlu.guidemate.screens.common.chat.model

import java.time.Instant

data class ChatMessage(
    val messageId: String,
    val chatId: String,
    val senderId: String,
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
