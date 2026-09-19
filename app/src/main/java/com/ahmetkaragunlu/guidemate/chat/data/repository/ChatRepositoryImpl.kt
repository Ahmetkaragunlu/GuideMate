package com.ahmetkaragunlu.guidemate.chat.data.repository

import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.chat.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.chat.data.realtime.ChatRealtimeEvent
import com.ahmetkaragunlu.guidemate.chat.data.realtime.ChatRealtimeSessionManager
import com.ahmetkaragunlu.guidemate.chat.data.realtime.ChatSessionSnapshot
import com.ahmetkaragunlu.guidemate.chat.data.remote.api.ChatApi
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ClearChatRequestDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.SendChatMessageRequestDto
import com.ahmetkaragunlu.guidemate.chat.data.state.ChatStateStore
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatConversation
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessage
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageDeliveryStatus
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageHistory
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatParticipant
import com.ahmetkaragunlu.guidemate.chat.domain.repository.ChatRepository
import com.ahmetkaragunlu.guidemate.common.coroutines.ApplicationScope
import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.result.mapSuccess
import java.time.Instant
import java.util.UUID
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val MESSAGE_PAGE_SIZE = 50

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi,
    private val realtimeSessionManager: ChatRealtimeSessionManager,
    private val userRepository: UserRepository,
    private val apiCallExecutor: ApiCallExecutor,
    private val stateStore: ChatStateStore,
    @param:ApplicationScope private val applicationScope: CoroutineScope,
) : ChatRepository {
    override val conversations = stateStore.conversations
    override val totalUnreadCount = stateStore.totalUnreadCount

    private val sessionGeneration = AtomicLong(0L)

    init {
        observeAuthenticatedUser()
    }

    override fun observeMessages(chatId: String): Flow<ChatMessageHistory> =
        stateStore.observeMessages(chatId)

    override suspend fun refreshConversations(): DataResult<List<ChatConversation>> {
        val session = currentSession()
        return apiCallExecutor.execute(
            request = api::getConversations,
            transform = { responses ->
                responses.map { it.toDomain() }.sortedByDescending(ChatConversation::lastActivityAt)
            },
        ).also { result ->
            if (result is DataResult.Success && session.isCurrent()) {
                stateStore.replaceConversations(result.data)
            }
        }
    }

    override suspend fun refreshUnreadCount(): DataResult<Int> {
        val session = currentSession()
        return apiCallExecutor.execute(
            request = api::getUnreadCount,
            transform = { it.unreadCount.toSafeInt() },
        ).also { result ->
            if (result is DataResult.Success && session.isCurrent()) {
                stateStore.setTotalUnreadCount(result.data)
            }
        }
    }

    override suspend fun loadInitialMessages(chatId: String): DataResult<ChatMessageHistory> {
        val session = currentSession()
        return apiCallExecutor.execute(
            request = { api.getMessages(chatId = chatId, size = MESSAGE_PAGE_SIZE) },
            transform = { it.toDomain() },
        ).also { result ->
            if (result is DataResult.Success && session.isCurrent()) {
                stateStore.mergeInitialMessages(chatId, result.data)
            }
        }.mapSuccess { loaded ->
            if (session.isCurrent()) stateStore.currentHistory(chatId) else loaded
        }
    }

    override suspend fun loadOlderMessages(chatId: String): DataResult<ChatMessageHistory> {
        val session = currentSession()
        val current = stateStore.currentHistory(chatId)
        if (!current.hasMore || current.nextCursor == null) return DataResult.Success(current)

        return apiCallExecutor.execute(
            request = {
                api.getMessages(
                    chatId = chatId,
                    before = current.nextCursor,
                    size = MESSAGE_PAGE_SIZE,
                )
            },
            transform = { it.toDomain() },
        ).also { result ->
            if (result is DataResult.Success && session.isCurrent()) {
                stateStore.mergeOlderMessages(chatId, result.data)
            }
        }.mapSuccess { loaded ->
            if (session.isCurrent()) stateStore.currentHistory(chatId) else loaded
        }
    }

    override suspend fun sendMessage(
        chatId: String,
        text: String,
    ): DataResult<ChatMessage> {
        val senderId = userRepository.userState.value.userId
            ?: return DataResult.Error(AppError.SessionExpired)
        val body = text.trim()
        if (body.isEmpty()) return DataResult.Error(AppError.GenericFailure)
        val clientMessageId = UUID.randomUUID().toString()
        val session = currentSession()
        val pendingMessage =
            ChatMessage(
                messageId = clientMessageId,
                chatId = chatId,
                senderId = senderId,
                clientMessageId = clientMessageId,
                text = body,
                sentAt = Instant.now(),
                deliveryStatus = ChatMessageDeliveryStatus.PENDING,
            )
        if (session.isCurrent()) stateStore.upsertMessage(pendingMessage)
        return sendPendingMessage(pendingMessage, session)
    }

    override suspend fun retryMessage(
        chatId: String,
        clientMessageId: String,
    ): DataResult<ChatMessage> {
        val message =
            stateStore.currentHistory(chatId).messages.firstOrNull {
                it.clientMessageId == clientMessageId &&
                    it.deliveryStatus == ChatMessageDeliveryStatus.FAILED
            } ?: return DataResult.Error(AppError.GenericFailure)
        val pending = message.copy(deliveryStatus = ChatMessageDeliveryStatus.PENDING)
        val session = currentSession()
        if (session.isCurrent()) stateStore.upsertMessage(pending)
        return sendPendingMessage(pending, session)
    }

    override suspend fun markRead(chatId: String): DataResult<Int> {
        val session = currentSession()
        return apiCallExecutor.execute(
            request = { api.markRead(chatId) },
            transform = { it.unreadCount.toSafeInt() },
        ).also { result ->
            if (result is DataResult.Success && session.isCurrent()) {
                stateStore.setTotalUnreadCount(result.data)
                stateStore.updateConversations { conversations ->
                    conversations.map { conversation ->
                        if (conversation.chatId == chatId) {
                            conversation.copy(unreadCount = 0)
                        } else {
                            conversation
                        }
                    }
                }
            }
        }
    }

    override suspend fun clearConversation(chatId: String): DataResult<Int> {
        val session = currentSession()
        return apiCallExecutor.execute(
            request = {
                api.clearConversation(
                    chatId = chatId,
                    request = ClearChatRequestDto(clientRequestId = UUID.randomUUID().toString()),
                )
            },
            transform = { it.unreadCount.toSafeInt() },
        ).also { result ->
            if (result is DataResult.Success && session.isCurrent()) {
                stateStore.setTotalUnreadCount(result.data)
                stateStore.removeConversation(chatId)
            }
        }
    }

    override suspend fun findOrCreate(remoteUserId: Long): DataResult<ChatConversation> {
        val session = currentSession()
        return apiCallExecutor.execute(
            request = { api.findOrCreate(remoteUserId) },
            transform = { it.toDomain() },
        ).also { result ->
            if (result is DataResult.Success && session.isCurrent()) {
                stateStore.updateConversations { conversations ->
                    (conversations.filterNot { it.chatId == result.data.chatId } + result.data)
                        .sortedByDescending(ChatConversation::lastActivityAt)
                }
            }
        }
    }

    private suspend fun sendPendingMessage(
        message: ChatMessage,
        session: ChatSessionSnapshot,
    ): DataResult<ChatMessage> {
        val result =
            apiCallExecutor.execute(
                request = {
                    api.sendMessage(
                        chatId = message.chatId,
                        request =
                            SendChatMessageRequestDto(
                                clientMessageId = message.clientMessageId,
                                body = message.text,
                            ),
                    )
                },
                transform = { it.toDomain() },
            )
        when (result) {
            is DataResult.Success -> {
                if (session.isCurrent()) {
                    stateStore.upsertMessage(result.data)
                    refreshConversations()
                    refreshUnreadCount()
                }
            }
            is DataResult.Error -> if (session.isCurrent()) stateStore.markMessageFailed(message)
        }
        return result
    }

    private suspend fun handleRealtimeEvent(
        session: ChatSessionSnapshot,
        event: ChatRealtimeEvent,
    ) {
        if (!session.isCurrent()) return
        when (event) {
            ChatRealtimeEvent.Connected -> resyncCanonicalState(session)
            ChatRealtimeEvent.Disconnected,
            is ChatRealtimeEvent.Error,
            -> Unit
            is ChatRealtimeEvent.MessageReceived -> {
                val message = event.message.toDomain()
                stateStore.upsertTrackedMessage(message)
                refreshConversations()
                refreshUnreadCount()
            }
            is ChatRealtimeEvent.ParticipantProfileUpdated -> {
                val participant = event.participant
                stateStore.updateConversations { conversations ->
                    conversations.map { conversation ->
                        conversation.copy(
                            guide =
                                conversation.guide.withUpdatedAvatar(
                                    participant.userId,
                                    participant.avatarUrl,
                                ),
                            tourist =
                                conversation.tourist.withUpdatedAvatar(
                                    participant.userId,
                                    participant.avatarUrl,
                                ),
                        )
                    }
                }
            }
        }
    }

    private fun observeAuthenticatedUser() {
        applicationScope.launch {
            var isInitialState = true
            userRepository.userState
                .map { it.userId }
                .distinctUntilChanged()
                .collectLatest { userId ->
                    if (isInitialState) {
                        isInitialState = false
                    } else {
                        sessionGeneration.incrementAndGet()
                    }
                    realtimeSessionManager.stop()
                    stateStore.clear()
                    if (userId != null) {
                        val session = currentSession()
                        realtimeSessionManager.start(
                            session = session,
                            onEvent = { event -> handleRealtimeEvent(session, event) },
                            onReconnect = {
                                if (session.isCurrent()) {
                                    refreshConversations()
                                    refreshUnreadCount()
                                }
                            },
                        )
                        refreshConversations()
                        refreshUnreadCount()
                        if (session.isCurrent()) realtimeSessionManager.connect()
                    }
                }
        }
    }

    private suspend fun resyncCanonicalState(session: ChatSessionSnapshot) {
        if (!session.isCurrent()) return
        refreshConversations()
        refreshUnreadCount()
        if (session.isCurrent()) {
            stateStore.observedChatIds().forEach { chatId -> loadInitialMessages(chatId) }
        }
    }

    private fun currentSession(): ChatSessionSnapshot =
        ChatSessionSnapshot(
            userId = userRepository.userState.value.userId,
            generation = sessionGeneration.get(),
        )

    private fun ChatSessionSnapshot.isCurrent(): Boolean =
        generation == sessionGeneration.get() && userId == userRepository.userState.value.userId

    private fun Long.toSafeInt(): Int = coerceIn(0, Int.MAX_VALUE.toLong()).toInt()
}

private fun ChatParticipant.withUpdatedAvatar(
    userId: Long,
    avatarUrl: String,
) = if (this.userId == userId) copy(avatarUrl = avatarUrl) else this
