package com.ahmetkaragunlu.guidemate.chat.data.repository

import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.chat.data.realtime.ChatRealtimeClient
import com.ahmetkaragunlu.guidemate.chat.data.realtime.ChatRealtimeEvent
import com.ahmetkaragunlu.guidemate.chat.data.remote.api.ChatApi
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatConversationResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatMessagePageResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatMessageResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.SendChatMessageRequestDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.UnreadCountResponseDto
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageDeliveryStatus
import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.google.gson.Gson
import java.time.Instant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class ChatRepositoryImplTest {
    @Test
    fun `send replaces optimistic message with canonical backend response`() = runBlocking {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        try {
            val api = FakeChatApi()
            val userRepository = FakeUserRepository()
            val repository = createRepository(api, userRepository, scope)
            repository.observeMessages(CHAT_ID).first()
            userRepository.state.value = UserState(userId = 42, email = "tourist@example.com")
            yield()

            val result = repository.sendMessage(CHAT_ID, "  Merhaba  ")

            assertTrue(result is DataResult.Success)
            assertEquals("Merhaba", api.lastSendRequest?.body)
            val message = repository.observeMessages(CHAT_ID).first().messages.single()
            assertEquals("server-message", message.messageId)
            assertEquals(api.lastSendRequest?.clientMessageId, message.clientMessageId)
            assertEquals(ChatMessageDeliveryStatus.SENT, message.deliveryStatus)
        } finally {
            scope.cancel()
        }
    }

    @Test
    fun `cursor pages merge in chronological order without duplicates`() = runBlocking {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        try {
            val api = FakeChatApi()
            val repository = createRepository(api, FakeUserRepository(), scope)

            repository.loadInitialMessages(CHAT_ID)
            val result = repository.loadOlderMessages(CHAT_ID)

            assertTrue(result is DataResult.Success)
            val history = (result as DataResult.Success).data
            assertEquals(listOf("old", "middle", "new"), history.messages.map { it.messageId })
            assertTrue(!history.hasMore)
        } finally {
            scope.cancel()
        }
    }

    private fun createRepository(
        api: ChatApi,
        userRepository: UserRepository,
        scope: CoroutineScope,
    ): ChatRepositoryImpl =
        ChatRepositoryImpl(
            api = api,
            realtimeClient = FakeRealtimeClient(),
            userRepository = userRepository,
            apiErrorParser = ApiErrorParser(Gson()),
            networkExceptionMapper = NetworkExceptionMapper(),
            applicationScope = scope,
        )

    private class FakeUserRepository : UserRepository {
        val state = MutableStateFlow(UserState())
        override val userState: StateFlow<UserState> = state

        override suspend fun restoreCachedUser(): UserState = state.value
    }

    private class FakeRealtimeClient : ChatRealtimeClient {
        private val mutableEvents = MutableSharedFlow<ChatRealtimeEvent>(extraBufferCapacity = 1)
        override val events: Flow<ChatRealtimeEvent> = mutableEvents

        override fun connect() = Unit

        override fun disconnect() = Unit
    }

    private class FakeChatApi : ChatApi {
        var lastSendRequest: SendChatMessageRequestDto? = null

        override suspend fun getConversations(): Response<List<ChatConversationResponseDto>> =
            Response.success(emptyList())

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
            return Response.success(
                message(
                    id = "server-message",
                    epochSecond = 40,
                    clientMessageId = request.clientMessageId,
                    body = request.body,
                ),
            )
        }

        override suspend fun markRead(chatId: String): Response<UnreadCountResponseDto> =
            Response.success(UnreadCountResponseDto(0))

        override suspend fun getUnreadCount(): Response<UnreadCountResponseDto> =
            Response.success(UnreadCountResponseDto(0))

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
    }

    private companion object {
        const val CHAT_ID = "00000000-0000-0000-0000-000000000001"
    }
}
