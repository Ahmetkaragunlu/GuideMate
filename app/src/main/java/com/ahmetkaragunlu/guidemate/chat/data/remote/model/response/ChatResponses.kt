package com.ahmetkaragunlu.guidemate.chat.data.remote.model.response

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class ChatParticipantResponse(
    @SerializedName("userId") val userId: Long,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("avatarUrl") val avatarUrl: String?,
)

data class ChatConversationResponse(
    @SerializedName("chatId") val chatId: String,
    @SerializedName("guide") val guide: ChatParticipantResponse,
    @SerializedName("tourist") val tourist: ChatParticipantResponse,
    @SerializedName("lastMessage") val lastMessage: ChatMessageResponse?,
    @SerializedName("unreadCount") val unreadCount: Long,
    @SerializedName("createdAt") val createdAt: Instant,
    @SerializedName("lastActivityAt") val lastActivityAt: Instant,
)

data class ChatMessageResponse(
    @SerializedName("messageId") val messageId: String,
    @SerializedName("chatId") val chatId: String,
    @SerializedName("senderId") val senderId: Long,
    @SerializedName("clientMessageId") val clientMessageId: String,
    @SerializedName("body") val body: String,
    @SerializedName("sentAt") val sentAt: Instant,
    @SerializedName("deliveryStatus") val deliveryStatus: String,
)

data class ChatMessagePageResponse(
    @SerializedName("content") val content: List<ChatMessageResponse>,
    @SerializedName("nextCursor") val nextCursor: String?,
    @SerializedName("hasNext") val hasNext: Boolean,
)

data class UnreadCountResponse(
    @SerializedName("unreadCount") val unreadCount: Long,
)

data class ChatRealtimeErrorResponse(
    @SerializedName("code") val code: String?,
)

data class ChatParticipantProfileUpdatedResponse(
    @SerializedName("userId") val userId: Long,
    @SerializedName("avatarUrl") val avatarUrl: String,
)
