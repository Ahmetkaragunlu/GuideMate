package com.ahmetkaragunlu.guidemate.chat.presentation.viewmodel

import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatConversation
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessage
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageHistory
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatParticipant
import com.ahmetkaragunlu.guidemate.chat.domain.repository.ChatRepository
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetType
import com.ahmetkaragunlu.guidemate.testing.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.FakeUserRepository
import com.ahmetkaragunlu.guidemate.testing.authenticatedUser
import java.time.Instant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatListViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `successful clear removes chat and marks related notifications read`() = runTest {
        val chatRepository = FakeChatRepository()
        val notificationRepository = FakeNotificationRepository()
        val viewModel = createViewModel(chatRepository, notificationRepository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        runCurrent()

        viewModel.clearConversation(CHAT_ID)
        runCurrent()

        assertEquals(listOf(CHAT_ID), chatRepository.clearRequests)
        assertEquals(0, viewModel.uiState.value.chats.size)
        assertEquals(NotificationTargetType.CHAT, notificationRepository.markedRelatedTargets.single().type)
        assertEquals(CHAT_ID, notificationRepository.markedRelatedTargets.single().targetId)
    }

    @Test
    fun `failed clear keeps chat and exposes user message`() = runTest {
        val chatRepository =
            FakeChatRepository().apply {
                clearResult = DataResult.Error(AppError.NoInternet)
            }
        val viewModel = createViewModel(chatRepository, FakeNotificationRepository())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        runCurrent()

        viewModel.clearConversation(CHAT_ID)
        runCurrent()

        assertEquals(1, viewModel.uiState.value.chats.size)
        assertNotNull(viewModel.uiState.value.userMessage)
    }

    private fun createViewModel(
        chatRepository: ChatRepository,
        notificationRepository: FakeNotificationRepository,
    ) = ChatListViewModel(
        chatRepository = chatRepository,
        notificationRepository = notificationRepository,
        userRepository = FakeUserRepository(authenticatedUser()),
        resourceProvider = FakeResourceProvider(),
    )

    private class FakeChatRepository : ChatRepository {
        private val conversation =
            ChatConversation(
                chatId = CHAT_ID,
                guide = ChatParticipant(userId = 10, displayName = "Guide", avatarUrl = null),
                tourist = ChatParticipant(userId = 7, displayName = "Tourist", avatarUrl = null),
                lastMessage = null,
                unreadCount = 1,
                createdAt = Instant.EPOCH,
                lastActivityAt = Instant.EPOCH,
            )
        private val conversationState = MutableStateFlow(listOf(conversation))
        override val conversations: StateFlow<List<ChatConversation>> = conversationState
        override val totalUnreadCount: StateFlow<Int> = MutableStateFlow(1)
        val clearRequests = mutableListOf<String>()
        var clearResult: DataResult<Int> = DataResult.Success(0)

        override fun observeMessages(chatId: String): Flow<ChatMessageHistory> =
            flowOf(ChatMessageHistory())

        override suspend fun refreshConversations(): DataResult<List<ChatConversation>> =
            DataResult.Success(conversationState.value)

        override suspend fun refreshUnreadCount(): DataResult<Int> = DataResult.Success(1)

        override suspend fun loadInitialMessages(chatId: String): DataResult<ChatMessageHistory> =
            DataResult.Success(ChatMessageHistory())

        override suspend fun loadOlderMessages(chatId: String): DataResult<ChatMessageHistory> =
            DataResult.Success(ChatMessageHistory())

        override suspend fun sendMessage(chatId: String, text: String): DataResult<ChatMessage> =
            error("Not required")

        override suspend fun retryMessage(
            chatId: String,
            clientMessageId: String,
        ): DataResult<ChatMessage> = error("Not required")

        override suspend fun markRead(chatId: String): DataResult<Int> = DataResult.Success(0)

        override suspend fun clearConversation(chatId: String): DataResult<Int> {
            clearRequests += chatId
            if (clearResult is DataResult.Success) conversationState.value = emptyList()
            return clearResult
        }

        override suspend fun findOrCreate(remoteUserId: Long): DataResult<ChatConversation> =
            DataResult.Success(conversation)
    }

    private companion object {
        const val CHAT_ID = "00000000-0000-0000-0000-000000000001"
    }
}
