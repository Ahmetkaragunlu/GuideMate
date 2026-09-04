package com.ahmetkaragunlu.guidemate.chat.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatConversation
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessage
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageDeliveryStatus
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageHistory
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatParticipant
import com.ahmetkaragunlu.guidemate.chat.domain.repository.ChatRepository
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetType
import com.ahmetkaragunlu.guidemate.testing.FakeNotificationRepository
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
import org.junit.Assert.assertTrue
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
            val notificationRepository = FakeNotificationRepository()
            createViewModel(repository, notificationRepository)
            runCurrent()

            assertEquals(1, repository.markReadCalls)
            assertEquals(NotificationTargetType.CHAT, notificationRepository.markedRelatedTargets.single().type)
            assertEquals(CHAT_ID, notificationRepository.markedRelatedTargets.single().targetId)

            repository.updateMessages(message("local-pending", CURRENT_USER_ID, ChatMessageDeliveryStatus.PENDING))
            runCurrent()
            repository.updateMessages(message("local-sent", CURRENT_USER_ID, ChatMessageDeliveryStatus.SENT))
            runCurrent()

            assertEquals(1, repository.markReadCalls)

            repository.updateMessages(message("remote-sent", REMOTE_USER_ID, ChatMessageDeliveryStatus.SENT))
            runCurrent()

            assertEquals(2, repository.markReadCalls)

            repository.updateMessages(message("next-local", CURRENT_USER_ID, ChatMessageDeliveryStatus.PENDING))
            runCurrent()

            assertEquals(2, repository.markReadCalls)
        }

    @Test
    fun `resume refreshes cached conversation participant`() =
        runTest {
            val repository = FakeChatRepository()
            val viewModel = createViewModel(repository, FakeNotificationRepository())
            runCurrent()

            viewModel.refreshParticipant()
            runCurrent()

            assertEquals(1, repository.refreshConversationCalls)
            assertEquals(
                "https://example.com/new-avatar.jpg",
                repository.conversations.value.single().guide.avatarUrl,
            )
        }

    @Test
    fun `failed detail load does not mark related notification read`() =
        runTest {
            val repository = FakeChatRepository().apply {
                initialMessagesResult = DataResult.Error(AppError.NoInternet)
            }
            val notificationRepository = FakeNotificationRepository()

            createViewModel(repository, notificationRepository)
            runCurrent()

            assertEquals(0, repository.markReadCalls)
            assertTrue(notificationRepository.markedRelatedTargets.isEmpty())
        }

    private fun createViewModel(
        repository: ChatRepository,
        notificationRepository: FakeNotificationRepository,
    ) =
        ChatDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("chatId" to CHAT_ID)),
            chatRepository = repository,
            userRepository =
                FakeUserRepository(
                    authenticatedUser().copy(userId = CURRENT_USER_ID),
                ),
            notificationRepository = notificationRepository,
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
        private val conversationState = MutableStateFlow<List<ChatConversation>>(emptyList())
        override val conversations: StateFlow<List<ChatConversation>> = conversationState
        override val totalUnreadCount: StateFlow<Int> = MutableStateFlow(0)
        var initialMessagesResult: DataResult<ChatMessageHistory> = DataResult.Success(history.value)
        var markReadCalls = 0
        var refreshConversationCalls = 0

        fun updateMessages(message: ChatMessage) {
            history.value = history.value.copy(messages = history.value.messages + message)
        }

        override fun observeMessages(chatId: String): Flow<ChatMessageHistory> = history

        override suspend fun loadInitialMessages(chatId: String): DataResult<ChatMessageHistory> =
            initialMessagesResult

        override suspend fun markRead(chatId: String): DataResult<Int> {
            markReadCalls++
            return DataResult.Success(0)
        }

        override suspend fun refreshConversations(): DataResult<List<ChatConversation>> =
            listOf(
                ChatConversation(
                    chatId = CHAT_ID,
                    guide =
                        ChatParticipant(
                            userId = REMOTE_USER_ID,
                            displayName = "Guide",
                            avatarUrl = "https://example.com/new-avatar.jpg",
                        ),
                    tourist = ChatParticipant(CURRENT_USER_ID, "Tourist"),
                    lastMessage = null,
                    unreadCount = 0,
                    createdAt = Instant.parse("2026-09-01T12:00:00Z"),
                    lastActivityAt = Instant.parse("2026-09-01T12:00:00Z"),
                ),
            ).let { conversations ->
                refreshConversationCalls++
                conversationState.value = conversations
                DataResult.Success(conversations)
            }

        override suspend fun refreshUnreadCount(): DataResult<Int> = error("Not used")

        override suspend fun loadOlderMessages(chatId: String): DataResult<ChatMessageHistory> =
            error("Not used")

        override suspend fun sendMessage(chatId: String, text: String): DataResult<ChatMessage> =
            error("Not used")

        override suspend fun retryMessage(
            chatId: String,
            clientMessageId: String,
        ): DataResult<ChatMessage> = error("Not used")

        override suspend fun clearConversation(chatId: String): DataResult<Int> = error("Not used")

        override suspend fun findOrCreate(remoteUserId: Long): DataResult<ChatConversation> =
            error("Not used")
    }

    private companion object {
        const val CHAT_ID = "chat-1"
        const val CURRENT_USER_ID = 7L
        const val REMOTE_USER_ID = 42L
    }
}
