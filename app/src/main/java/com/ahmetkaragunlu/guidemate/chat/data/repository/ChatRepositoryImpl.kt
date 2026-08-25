package com.ahmetkaragunlu.guidemate.chat.data.repository

import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.chat.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.chat.data.realtime.ChatRealtimeClient
import com.ahmetkaragunlu.guidemate.chat.data.realtime.ChatRealtimeEvent
import com.ahmetkaragunlu.guidemate.chat.data.remote.api.ChatApi
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.SendChatMessageRequestDto
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatConversation
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessage
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageDeliveryStatus
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageHistory
import com.ahmetkaragunlu.guidemate.chat.domain.repository.ChatRepository
import com.ahmetkaragunlu.guidemate.common.coroutines.ApplicationScope
import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.result.mapSuccess
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val MESSAGE_PAGE_SIZE = 50
private const val MAX_RECONNECT_DELAY_SECONDS = 30L

@Singleton
class ChatRepositoryImpl
@Inject
constructor(
    private val api: ChatApi,
    private val realtimeClient: ChatRealtimeClient,
    private val userRepository: UserRepository,
    private val apiCallExecutor: ApiCallExecutor,
    @param:ApplicationScope private val applicationScope: CoroutineScope,
) : ChatRepository {
    private val mutableConversations = MutableStateFlow<List<ChatConversation>>(emptyList())
    override val conversations: StateFlow<List<ChatConversation>> =
        mutableConversations.asStateFlow()

    private val mutableTotalUnreadCount = MutableStateFlow(0)
    override val totalUnreadCount: StateFlow<Int> = mutableTotalUnreadCount.asStateFlow()

    private val messageHistories =
        ConcurrentHashMap<String, MutableStateFlow<ChatMessageHistory>>()
    private var reconnectAttempt = 0
    private var reconnectJob: Job? = null

    init {
        observeRealtimeEvents()
        observeAuthenticatedUser()
    }

    override fun observeMessages(chatId: String): Flow<ChatMessageHistory> =
        messageHistory(chatId).asStateFlow()

    override suspend fun refreshConversations(): DataResult<List<ChatConversation>> =
        apiCallExecutor.execute(
            request = api::getConversations,
            transform = { responses ->
                responses.map { it.toDomain() }.sortedByDescending(ChatConversation::lastActivityAt)
            },
        ).also { result ->
            if (result is DataResult.Success) mutableConversations.value = result.data
        }

    override suspend fun refreshUnreadCount(): DataResult<Int> =
        apiCallExecutor.execute(
            request = api::getUnreadCount,
            transform = { it.unreadCount.toSafeInt() },
        ).also { result ->
            if (result is DataResult.Success) mutableTotalUnreadCount.value = result.data
        }

    override suspend fun loadInitialMessages(chatId: String): DataResult<ChatMessageHistory> =
        apiCallExecutor.execute(
            request = { api.getMessages(chatId = chatId, size = MESSAGE_PAGE_SIZE) },
            transform = { it.toDomain() },
        ).also { result ->
            if (result is DataResult.Success) {
                messageHistory(chatId).update { current ->
                    result.data.copy(
                        messages = mergeMessages(current.messages, result.data.messages),
                    )
                }
            }
        }.mapSuccess { messageHistory(chatId).value }

    override suspend fun loadOlderMessages(chatId: String): DataResult<ChatMessageHistory> {
        val current = messageHistory(chatId).value
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
            if (result is DataResult.Success) {
                messageHistory(chatId).update { latest ->
                    result.data.copy(
                        messages = mergeMessages(result.data.messages, latest.messages),
                    )
                }
            }
        }.mapSuccess { messageHistory(chatId).value }
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
        upsertMessage(pendingMessage)
        return sendPendingMessage(pendingMessage)
    }

    override suspend fun retryMessage(
        chatId: String,
        clientMessageId: String,
    ): DataResult<ChatMessage> {
        val message =
            messageHistory(chatId).value.messages.firstOrNull {
                it.clientMessageId == clientMessageId &&
                    it.deliveryStatus == ChatMessageDeliveryStatus.FAILED
            } ?: return DataResult.Error(AppError.GenericFailure)
        val pending = message.copy(deliveryStatus = ChatMessageDeliveryStatus.PENDING)
        upsertMessage(pending)
        return sendPendingMessage(pending)
    }

    override suspend fun markRead(chatId: String): DataResult<Int> =
        apiCallExecutor.execute(
            request = { api.markRead(chatId) },
            transform = { it.unreadCount.toSafeInt() },
        ).also { result ->
            if (result is DataResult.Success) {
                mutableTotalUnreadCount.value = result.data
                mutableConversations.update { conversations ->
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

    override suspend fun findOrCreate(remoteUserId: Long): DataResult<ChatConversation> =
        apiCallExecutor.execute(
            request = { api.findOrCreate(remoteUserId) },
            transform = { it.toDomain() },
        ).also { result ->
            if (result is DataResult.Success) {
                mutableConversations.update { conversations ->
                    (conversations.filterNot { it.chatId == result.data.chatId } + result.data)
                        .sortedByDescending(ChatConversation::lastActivityAt)
                }
            }
        }

    private suspend fun sendPendingMessage(message: ChatMessage): DataResult<ChatMessage> {
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
                upsertMessage(result.data)
                refreshConversations()
                refreshUnreadCount()
            }
            is DataResult.Error -> markMessageFailed(message)
        }
        return result
    }

    private fun observeRealtimeEvents() {
        applicationScope.launch {
            realtimeClient.events.collect { event ->
                when (event) {
                    ChatRealtimeEvent.Connected -> {
                        reconnectAttempt = 0
                        reconnectJob?.cancel()
                        resyncCanonicalState()
                    }
                    ChatRealtimeEvent.Disconnected -> scheduleReconnect()
                    is ChatRealtimeEvent.MessageReceived -> {
                        val message = event.message.toDomain()
                        messageHistories[message.chatId]?.let { upsertMessage(message) }
                        refreshConversations()
                        refreshUnreadCount()
                    }
                    is ChatRealtimeEvent.Error -> Unit
                }
            }
        }
    }

    private fun observeAuthenticatedUser() {
        applicationScope.launch {
            userRepository.userState
                .map { it.userId }
                .distinctUntilChanged()
                .collect { userId ->
                    reconnectJob?.cancel()
                    realtimeClient.disconnect()
                    clearCachedState()
                    if (userId != null) {
                        refreshConversations()
                        refreshUnreadCount()
                        realtimeClient.connect()
                    }
                }
        }
    }

    private suspend fun resyncCanonicalState() {
        refreshConversations()
        refreshUnreadCount()
        messageHistories.keys.toList().forEach { chatId -> loadInitialMessages(chatId) }
    }

    private fun scheduleReconnect() {
        if (userRepository.userState.value.userId == null || reconnectJob?.isActive == true) return
        reconnectJob =
            applicationScope.launch {
                val delaySeconds = min(1L shl reconnectAttempt.coerceAtMost(5), MAX_RECONNECT_DELAY_SECONDS)
                reconnectAttempt++
                delay(delaySeconds * 1_000)
                refreshConversations()
                refreshUnreadCount()
                realtimeClient.connect()
            }
    }

    private fun clearCachedState() {
        mutableConversations.value = emptyList()
        mutableTotalUnreadCount.value = 0
        messageHistories.values.forEach { it.value = ChatMessageHistory() }
        messageHistories.clear()
    }

    private fun messageHistory(chatId: String): MutableStateFlow<ChatMessageHistory> =
        messageHistories.getOrPut(chatId) { MutableStateFlow(ChatMessageHistory()) }

    private fun upsertMessage(message: ChatMessage) {
        messageHistory(message.chatId).update { history ->
            history.copy(messages = mergeMessages(history.messages, listOf(message)))
        }
    }

    private fun markMessageFailed(message: ChatMessage) {
        messageHistory(message.chatId).update { history ->
            val messages =
                history.messages.map { existing ->
                    if (existing.clientMessageId == message.clientMessageId &&
                        existing.deliveryStatus != ChatMessageDeliveryStatus.SENT
                    ) {
                        existing.copy(deliveryStatus = ChatMessageDeliveryStatus.FAILED)
                    } else {
                        existing
                    }
                }
            history.copy(messages = messages)
        }
    }

    private fun mergeMessages(
        current: List<ChatMessage>,
        incoming: List<ChatMessage>,
    ): List<ChatMessage> {
        val messagesByClientId = LinkedHashMap<String, ChatMessage>()
        current.forEach { messagesByClientId[it.clientMessageId] = it }
        incoming.forEach { messagesByClientId[it.clientMessageId] = it }
        return messagesByClientId.values.sortedWith(
            compareBy(ChatMessage::sentAt, ChatMessage::messageId),
        )
    }
    private fun Long.toSafeInt(): Int = coerceIn(0, Int.MAX_VALUE.toLong()).toInt()
}
