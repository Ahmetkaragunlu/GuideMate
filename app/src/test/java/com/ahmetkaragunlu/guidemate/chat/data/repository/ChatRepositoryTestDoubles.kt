package com.ahmetkaragunlu.guidemate.chat.data.repository

import com.ahmetkaragunlu.guidemate.chat.data.realtime.ChatRealtimeClient
import com.ahmetkaragunlu.guidemate.chat.data.realtime.ChatRealtimeEvent
import com.ahmetkaragunlu.guidemate.chat.data.remote.api.ChatApi
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatConversationResponse
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatMessagePageResponse
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatMessageResponse
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatParticipantResponse
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.request.ClearChatRequest
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.request.SendChatMessageRequest
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.UnreadCountResponse
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
    var lastSendRequest: SendChatMessageRequest? = null
    var failNextSend = false
    var markReadUnreadCount = 0L
    var clearUnreadCount = 0L
    var failClear = false
    var lastMarkedReadChatId: String? = null
    var lastClearedChatId: String? = null
    var lastClearRequest: ClearChatRequest? = null
    var conversationCalls = 0
    var unreadCountCalls = 0
    var conversationResponses: List<ChatConversationResponse> = emptyList()
    var blockNextConversationRequest = false
    var conversationRequestStarted: CompletableDeferred<Unit>? = null
    var conversationResponseGate: CompletableDeferred<Unit>? = null

    override suspend fun getConversations(): Response<List<ChatConversationResponse>> {
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
    ): Response<ChatConversationResponse> = error("Not used")

    override suspend fun getMessages(
        chatId: String,
        before: String?,
        size: Int,
    ): Response<ChatMessagePageResponse> =
        if (before == null) {
            Response.success(
                ChatMessagePageResponse(
                    content = listOf(message("middle", 20), message("new", 30)),
                    nextCursor = "middle",
                    hasNext = true,
                ),
            )
        } else {
            Response.success(
                ChatMessagePageResponse(
                    content = listOf(message("old", 10), message("middle", 20)),
                    nextCursor = null,
                    hasNext = false,
                ),
            )
        }

    override suspend fun sendMessage(
        chatId: String,
        request: SendChatMessageRequest,
    ): Response<ChatMessageResponse> {
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

    override suspend fun markRead(chatId: String): Response<UnreadCountResponse> {
        lastMarkedReadChatId = chatId
        return Response.success(UnreadCountResponse(markReadUnreadCount))
    }

    override suspend fun clearConversation(
        chatId: String,
        request: ClearChatRequest,
    ): Response<UnreadCountResponse> {
        lastClearedChatId = chatId
        lastClearRequest = request
        return if (failClear) {
            Response.error(503, "temporary".toResponseBody())
        } else {
            Response.success(UnreadCountResponse(clearUnreadCount))
        }
    }

    override suspend fun getUnreadCount(): Response<UnreadCountResponse> {
        unreadCountCalls++
        return Response.success(UnreadCountResponse(0))
    }

    private fun message(
        id: String,
        epochSecond: Long,
        clientMessageId: String = "client-$id",
        body: String = id,
    ): ChatMessageResponse =
        ChatMessageResponse(
            messageId = id,
            chatId = CHAT_ID,
            senderId = 42,
            clientMessageId = clientMessageId,
            body = body,
            sentAt = Instant.ofEpochSecond(epochSecond),
            deliveryStatus = "SENT",
        )

    fun conversation(avatarUrl: String): ChatConversationResponse =
        ChatConversationResponse(
            chatId = CHAT_ID,
            guide = ChatParticipantResponse(99, "Guide", avatarUrl),
            tourist = ChatParticipantResponse(42, "Tourist", null),
            lastMessage = null,
            unreadCount = 0,
            createdAt = Instant.parse("2026-09-01T12:00:00Z"),
            lastActivityAt = Instant.parse("2026-09-01T12:00:00Z"),
        )
}
