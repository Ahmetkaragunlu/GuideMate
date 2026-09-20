package com.ahmetkaragunlu.guidemate.chat.data.repository

import com.ahmetkaragunlu.guidemate.chat.data.realtime.ChatRealtimeClient
import com.ahmetkaragunlu.guidemate.chat.data.realtime.ChatRealtimeEvent
import com.ahmetkaragunlu.guidemate.chat.data.remote.api.ChatApi
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatConversationResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatMessagePageResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatMessageResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatParticipantResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ClearChatRequestDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.SendChatMessageRequestDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.UnreadCountResponseDto
import java.time.Instant
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

internal const val CHAT_ID = "00000000-0000-0000-0000-000000000001"

internal class FakeRealtimeClient : ChatRealtimeClient {
    private val mutableEvents = MutableSharedFlow<ChatRealtimeEvent>(extraBufferCapacity = 1)
    override val events: Flow<ChatRealtimeEvent> = mutableEvents
    var connectCalls = 0

    override fun connect() {
        connectCalls++
    }

    override fun disconnect() = Unit

    fun emit(event: ChatRealtimeEvent) {
        mutableEvents.tryEmit(event)
    }
}

internal class FakeChatApi : ChatApi {
    var lastSendRequest: SendChatMessageRequestDto? = null
    var failNextSend = false
    var markReadUnreadCount = 0L
    var clearUnreadCount = 0L
    var failClear = false
    var lastMarkedReadChatId: String? = null
    var lastClearedChatId: String? = null
    var lastClearRequest: ClearChatRequestDto? = null
    var conversationCalls = 0
    var unreadCountCalls = 0
    var conversationResponses: List<ChatConversationResponseDto> = emptyList()
    var blockNextConversationRequest = false
    var conversationRequestStarted: CompletableDeferred<Unit>? = null
    var conversationResponseGate: CompletableDeferred<Unit>? = null

    override suspend fun getConversations(): Response<List<ChatConversationResponseDto>> {
        conversationCalls++
        val response = conversationResponses
        if (blockNextConversationRequest) {
            blockNextConversationRequest = false
            conversationRequestStarted?.complete(Unit)
            conversationResponseGate?.await()
        }
        return Response.success(response)
    }

    override suspend fun findOrCreate(
        remoteUserId: Long,
    ): Response<ChatConversationResponseDto> = error("Not used")

    override suspend fun getMessages(
        chatId: String,
        before: String?,
        size: Int,
    ): Response<ChatMessagePageResponseDto> =
        if (before == null) {
            Response.success(
                ChatMessagePageResponseDto(
                    content = listOf(message("middle", 20), message("new", 30)),
                    nextCursor = "middle",
                    hasNext = true,
                ),
            )
        } else {
            Response.success(
                ChatMessagePageResponseDto(
                    content = listOf(message("old", 10), message("middle", 20)),
                    nextCursor = null,
                    hasNext = false,
                ),
            )
        }

    override suspend fun sendMessage(
        chatId: String,
        request: SendChatMessageRequestDto,
    ): Response<ChatMessageResponseDto> {
        lastSendRequest = request
        if (failNextSend) {
            failNextSend = false
            return Response.error(503, "temporary".toResponseBody())
        }
        return Response.success(
            message(
                id = "server-message",
                epochSecond = 40,
                clientMessageId = request.clientMessageId,
                body = request.body,
            ),
        )
    }

    override suspend fun markRead(chatId: String): Response<UnreadCountResponseDto> {
        lastMarkedReadChatId = chatId
        return Response.success(UnreadCountResponseDto(markReadUnreadCount))
    }

    override suspend fun clearConversation(
        chatId: String,
        request: ClearChatRequestDto,
    ): Response<UnreadCountResponseDto> {
        lastClearedChatId = chatId
        lastClearRequest = request
        return if (failClear) {
            Response.error(503, "temporary".toResponseBody())
        } else {
            Response.success(UnreadCountResponseDto(clearUnreadCount))
        }
    }

    override suspend fun getUnreadCount(): Response<UnreadCountResponseDto> {
        unreadCountCalls++
        return Response.success(UnreadCountResponseDto(0))
    }

    private fun message(
        id: String,
        epochSecond: Long,
        clientMessageId: String = "client-$id",
        body: String = id,
    ): ChatMessageResponseDto =
        ChatMessageResponseDto(
            messageId = id,
            chatId = CHAT_ID,
            senderId = 42,
            clientMessageId = clientMessageId,
            body = body,
            sentAt = Instant.ofEpochSecond(epochSecond),
            deliveryStatus = "SENT",
        )

    fun conversation(avatarUrl: String): ChatConversationResponseDto =
        ChatConversationResponseDto(
            chatId = CHAT_ID,
            guide = ChatParticipantResponseDto(99, "Guide", avatarUrl),
            tourist = ChatParticipantResponseDto(42, "Tourist", null),
            lastMessage = null,
            unreadCount = 0,
            createdAt = Instant.parse("2026-09-01T12:00:00Z"),
            lastActivityAt = Instant.parse("2026-09-01T12:00:00Z"),
        )
}
