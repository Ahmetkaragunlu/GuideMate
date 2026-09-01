package com.ahmetkaragunlu.guidemate.chat.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatConversation
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessage
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageDeliveryStatus
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageHistory
import com.ahmetkaragunlu.guidemate.chat.domain.repository.ChatRepository
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.FakeUserRepository
import com.ahmetkaragunlu.guidemate.testing.authenticatedUser
import java.time.Instant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ChatDetailViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `marks only new remote sent messages as read`() =
        runTest {
            val repository = FakeChatRepository()
            createViewModel(repository)
            runCurrent()

            repository.updateMessages(message("local-pending", CURRENT_USER_ID, ChatMessageDeliveryStatus.PENDING))
            runCurrent()
            repository.updateMessages(message("local-sent", CURRENT_USER_ID, ChatMessageDeliveryStatus.SENT))
            runCurrent()

            assertEquals(0, repository.markReadCalls)

            repository.updateMessages(message("remote-sent", REMOTE_USER_ID, ChatMessageDeliveryStatus.SENT))
            runCurrent()

            assertEquals(1, repository.markReadCalls)

            repository.updateMessages(message("next-local", CURRENT_USER_ID, ChatMessageDeliveryStatus.PENDING))
            runCurrent()

            assertEquals(1, repository.markReadCalls)
        }

    private fun createViewModel(repository: ChatRepository) =
        ChatDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("chatId" to CHAT_ID)),
            chatRepository = repository,
            userRepository =
                FakeUserRepository(
                    authenticatedUser().copy(userId = CURRENT_USER_ID),
                ),
            resourceProvider = FakeResourceProvider(),
        )

    private fun message(
        id: String,
        senderId: Long,
        status: ChatMessageDeliveryStatus,
    ): ChatMessage =
        ChatMessage(
            messageId = id,
            chatId = CHAT_ID,
            senderId = senderId,
            clientMessageId = "client-$id",
            text = id,
            sentAt = Instant.parse("2026-09-01T12:00:00Z"),
            deliveryStatus = status,
        )

    private class FakeChatRepository : ChatRepository {
        private val history = MutableStateFlow(ChatMessageHistory())
        override val conversations: StateFlow<List<ChatConversation>> = MutableStateFlow(emptyList())
        override val totalUnreadCount: StateFlow<Int> = MutableStateFlow(0)
        var markReadCalls = 0

        fun updateMessages(message: ChatMessage) {
            history.value = history.value.copy(messages = history.value.messages + message)
        }

        override fun observeMessages(chatId: String): Flow<ChatMessageHistory> = history

        override suspend fun loadInitialMessages(chatId: String): DataResult<ChatMessageHistory> =
            DataResult.Success(history.value)

        override suspend fun markRead(chatId: String): DataResult<Int> {
            markReadCalls++
            return DataResult.Success(0)
        }

        override suspend fun refreshConversations(): DataResult<List<ChatConversation>> =
            error("Not used")

        override suspend fun refreshUnreadCount(): DataResult<Int> = error("Not used")

        override suspend fun loadOlderMessages(chatId: String): DataResult<ChatMessageHistory> =
            error("Not used")

        override suspend fun sendMessage(chatId: String, text: String): DataResult<ChatMessage> =
            error("Not used")

        override suspend fun retryMessage(
            chatId: String,
            clientMessageId: String,
        ): DataResult<ChatMessage> = error("Not used")

        override suspend fun findOrCreate(remoteUserId: Long): DataResult<ChatConversation> =
            error("Not used")
    }

    private companion object {
        const val CHAT_ID = "chat-1"
        const val CURRENT_USER_ID = 7L
        const val REMOTE_USER_ID = 42L
    }
}
