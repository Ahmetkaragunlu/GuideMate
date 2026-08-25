package com.ahmetkaragunlu.guidemate.chat.data.mapper

import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatConversationResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatMessagePageResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatMessageResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatParticipantResponseDto
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatConversation
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessage
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageDeliveryStatus
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageHistory
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatParticipant

fun ChatParticipantResponseDto.toDomain(): ChatParticipant =
    ChatParticipant(
        userId = userId,
        displayName = displayName,
        avatarUrl = avatarUrl,
    )

fun ChatMessageResponseDto.toDomain(): ChatMessage =
    ChatMessage(
        messageId = messageId,
        chatId = chatId,
        senderId = senderId,
        clientMessageId = clientMessageId,
        text = body,
        sentAt = sentAt,
        deliveryStatus = deliveryStatus.toDeliveryStatus(),
    )

fun ChatConversationResponseDto.toDomain(): ChatConversation =
    ChatConversation(
        chatId = chatId,
        guide = guide.toDomain(),
        tourist = tourist.toDomain(),
        lastMessage = lastMessage?.toDomain(),
        unreadCount = unreadCount.coerceIn(0, Int.MAX_VALUE.toLong()).toInt(),
        createdAt = createdAt,
        lastActivityAt = lastActivityAt,
    )

fun ChatMessagePageResponseDto.toDomain(): ChatMessageHistory =
    ChatMessageHistory(
        messages = content.map(ChatMessageResponseDto::toDomain),
        nextCursor = nextCursor,
        hasMore = hasNext,
    )

private fun String.toDeliveryStatus(): ChatMessageDeliveryStatus =
    ChatMessageDeliveryStatus.entries.firstOrNull { it.name == this }
        ?: ChatMessageDeliveryStatus.SENT
