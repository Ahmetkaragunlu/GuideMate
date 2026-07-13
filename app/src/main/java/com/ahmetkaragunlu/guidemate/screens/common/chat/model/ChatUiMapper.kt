package com.ahmetkaragunlu.guidemate.screens.common.chat.model

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

internal fun ChatConversation.toChatUiModel(currentUserId: String): ChatUiModel? {
    val remoteParticipant = otherParticipant(currentUserId) ?: return null
    val lastMessage = lastMessage

    return ChatUiModel(
        chatId = chatId,
        remoteUserId = remoteParticipant.userId,
        name = remoteParticipant.displayName,
        lastMessage = lastMessage?.text.orEmpty(),
        time = lastMessage?.sentAt?.toConversationTime().orEmpty(),
        avatarResId = remoteParticipant.avatarResId,
        avatarUrl = remoteParticipant.avatarUrl,
        unreadCount = unreadCount(currentUserId),
    )
}

internal fun ChatMessage.toMessageUiModel(currentUserId: String): MessageUiModel =
    MessageUiModel(
        messageId = messageId,
        text = text,
        time = sentAt.toMessageTime(),
        isFromMe = senderId == currentUserId,
        deliveryStatus = deliveryStatus,
    )

private fun Instant.toConversationTime(): String {
    val dateTime = atZone(ZoneId.systemDefault())
    val today = LocalDate.now(dateTime.zone)

    return when (dateTime.toLocalDate()) {
        today -> dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        today.minusDays(1) -> dateTime.format(DateTimeFormatter.ofPattern("EEE", Locale.getDefault()))
        else -> dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
    }
}

private fun Instant.toMessageTime(): String =
    atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
