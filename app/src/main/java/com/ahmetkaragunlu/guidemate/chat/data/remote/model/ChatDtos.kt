package com.ahmetkaragunlu.guidemate.chat.data.remote.model

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class ChatParticipantResponseDto(
    @SerializedName("userId") val userId: Long,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("avatarUrl") val avatarUrl: String?,
)

data class ChatConversationResponseDto(
    @SerializedName("chatId") val chatId: String,
    @SerializedName("guide") val guide: ChatParticipantResponseDto,
    @SerializedName("tourist") val tourist: ChatParticipantResponseDto,
    @SerializedName("lastMessage") val lastMessage: ChatMessageResponseDto?,
    @SerializedName("unreadCount") val unreadCount: Long,
    @SerializedName("createdAt") val createdAt: Instant,
    @SerializedName("lastActivityAt") val lastActivityAt: Instant,
)

data class ChatMessageResponseDto(
    @SerializedName("messageId") val messageId: String,
    @SerializedName("chatId") val chatId: String,
    @SerializedName("senderId") val senderId: Long,
    @SerializedName("clientMessageId") val clientMessageId: String,
    @SerializedName("body") val body: String,
    @SerializedName("sentAt") val sentAt: Instant,
    @SerializedName("deliveryStatus") val deliveryStatus: String,
)

data class ChatMessagePageResponseDto(
    @SerializedName("content") val content: List<ChatMessageResponseDto>,
    @SerializedName("nextCursor") val nextCursor: String?,
    @SerializedName("hasNext") val hasNext: Boolean,
)

data class SendChatMessageRequestDto(
    @SerializedName("clientMessageId") val clientMessageId: String,
    @SerializedName("body") val body: String,
)

data class ClearChatRequestDto(
    @SerializedName("clientRequestId") val clientRequestId: String,
)

data class UnreadCountResponseDto(
    @SerializedName("unreadCount") val unreadCount: Long,
)

data class ChatRealtimeErrorResponseDto(
    @SerializedName("code") val code: String?,
)

data class ChatParticipantProfileUpdatedResponseDto(
    @SerializedName("userId") val userId: Long,
    @SerializedName("avatarUrl") val avatarUrl: String,
)
