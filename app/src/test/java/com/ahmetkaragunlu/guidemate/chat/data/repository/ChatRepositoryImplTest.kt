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
import com.ahmetkaragunlu.guidemate.common.network.testApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import java.time.Instant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
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

    @Test
    fun `failed message retries with the same client id and becomes sent`() = runBlocking {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        try {
            val api = FakeChatApi().apply { failNextSend = true }
            val userRepository = FakeUserRepository()
            val repository = createRepository(api, userRepository, scope)
            repository.observeMessages(CHAT_ID).first()
            userRepository.state.value = UserState(userId = 42, email = "tourist@example.com")
            yield()

            val failedResult = repository.sendMessage(CHAT_ID, "Retry me")
            val failedMessage = repository.observeMessages(CHAT_ID).first().messages.single()

            assertTrue(failedResult is DataResult.Error)
            assertEquals(ChatMessageDeliveryStatus.FAILED, failedMessage.deliveryStatus)

            val retryResult = repository.retryMessage(CHAT_ID, failedMessage.clientMessageId)
            val sentMessage = repository.observeMessages(CHAT_ID).first().messages.single()

            assertTrue(retryResult is DataResult.Success)
            assertEquals(failedMessage.clientMessageId, api.lastSendRequest?.clientMessageId)
            assertEquals(ChatMessageDeliveryStatus.SENT, sentMessage.deliveryStatus)
        } finally {
            scope.cancel()
        }
    }

    @Test
    fun `mark read applies canonical unread count`() = runBlocking {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        try {
            val api = FakeChatApi().apply { markReadUnreadCount = 3 }
            val repository = createRepository(api, FakeUserRepository(), scope)

            val result = repository.markRead(CHAT_ID)

            assertTrue(result is DataResult.Success)
            assertEquals(3, repository.totalUnreadCount.value)
            assertEquals(CHAT_ID, api.lastMarkedReadChatId)
        } finally {
            scope.cancel()
        }
    }

    @Test
    fun `unexpected disconnect reconnects with backoff and resyncs canonical state`() = runTest {
        val api = FakeChatApi()
        val userRepository = FakeUserRepository()
        val realtimeClient = FakeRealtimeClient()
        createRepository(api, userRepository, backgroundScope, realtimeClient)

        userRepository.state.value = UserState(userId = 42, email = "tourist@example.com")
        runCurrent()
        assertEquals(1, realtimeClient.connectCalls)

        realtimeClient.emit(ChatRealtimeEvent.Disconnected)
        runCurrent()
        advanceTimeBy(999)
        runCurrent()
        assertEquals(1, realtimeClient.connectCalls)

        advanceTimeBy(1)
        runCurrent()
        assertEquals(2, realtimeClient.connectCalls)
        assertTrue(api.conversationCalls >= 2)
        assertTrue(api.unreadCountCalls >= 2)
    }

    private fun createRepository(
        api: ChatApi,
        userRepository: UserRepository,
        scope: CoroutineScope,
        realtimeClient: ChatRealtimeClient = FakeRealtimeClient(),
    ): ChatRepositoryImpl =
        ChatRepositoryImpl(
            api = api,
            realtimeClient = realtimeClient,
            userRepository = userRepository,
            apiCallExecutor = testApiCallExecutor(),
            applicationScope = scope,
        )

    private class FakeUserRepository : UserRepository {
        val state = MutableStateFlow(UserState())
        override val userState: StateFlow<UserState> = state

        override suspend fun restoreCachedUser(): UserState = state.value

        override suspend fun updateAvatar(mediaAssetId: String, imageUrl: String) {
            state.value = state.value.copy(avatarMediaId = mediaAssetId, avatarUrl = imageUrl)
        }
    }

    private class FakeRealtimeClient : ChatRealtimeClient {
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

    private class FakeChatApi : ChatApi {
        var lastSendRequest: SendChatMessageRequestDto? = null
        var failNextSend = false
        var markReadUnreadCount = 0L
        var lastMarkedReadChatId: String? = null
        var conversationCalls = 0
        var unreadCountCalls = 0

        override suspend fun getConversations(): Response<List<ChatConversationResponseDto>> {
            conversationCalls++
            return Response.success(emptyList())
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
    }

    private companion object {
        const val CHAT_ID = "00000000-0000-0000-0000-000000000001"
    }
}
