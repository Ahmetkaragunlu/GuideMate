package com.ahmetkaragunlu.guidemate.chat.presentation.model

import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageDeliveryStatus

data class MessageUiModel(
    val messageId: String,
    val text: String,
    val time: String,
    val isFromMe: Boolean,
    val deliveryStatus: ChatMessageDeliveryStatus,
)
