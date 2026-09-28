package com.ahmetkaragunlu.guidemate.chat.data.mapper

import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatConversationResponse
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatMessagePageResponse
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatMessageResponse
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatParticipantResponse
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatConversation
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessage
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageDeliveryStatus
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageHistory
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatParticipant

fun ChatParticipantResponse.toDomain(): ChatParticipant =
    ChatParticipant(
        userId = userId,
        displayName = displayName,
        avatarUrl = avatarUrl,
    )

fun ChatMessageResponse.toDomain(): ChatMessage =
    ChatMessage(
        messageId = messageId,
        chatId = chatId,
        senderId = senderId,
        clientMessageId = clientMessageId,
        text = body,
        sentAt = sentAt,
        deliveryStatus = deliveryStatus.toDeliveryStatus(),
    )

fun ChatConversationResponse.toDomain(): ChatConversation =
    ChatConversation(
        chatId = chatId,
        guide = guide.toDomain(),
        tourist = tourist.toDomain(),
        lastMessage = lastMessage?.toDomain(),
        unreadCount = unreadCount.coerceIn(0, Int.MAX_VALUE.toLong()).toInt(),
        createdAt = createdAt,
        lastActivityAt = lastActivityAt,
    )

fun ChatMessagePageResponse.toDomain(): ChatMessageHistory =
    ChatMessageHistory(
        messages = content.map(ChatMessageResponse::toDomain),
        nextCursor = nextCursor,
        hasMore = hasNext,
    )

private fun String.toDeliveryStatus(): ChatMessageDeliveryStatus =
    ChatMessageDeliveryStatus.entries.firstOrNull { it.name == this }
        ?: ChatMessageDeliveryStatus.SENT
