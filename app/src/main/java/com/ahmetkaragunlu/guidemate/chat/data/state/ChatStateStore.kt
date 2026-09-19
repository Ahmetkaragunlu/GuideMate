package com.ahmetkaragunlu.guidemate.chat.data.state

import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatConversation
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessage
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageDeliveryStatus
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageHistory
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatStateStore @Inject constructor() {
    private val mutableConversations = MutableStateFlow<List<ChatConversation>>(emptyList())
    val conversations: StateFlow<List<ChatConversation>> = mutableConversations.asStateFlow()

    private val mutableTotalUnreadCount = MutableStateFlow(0)
    val totalUnreadCount: StateFlow<Int> = mutableTotalUnreadCount.asStateFlow()

    private val messageHistories =
        ConcurrentHashMap<String, MutableStateFlow<ChatMessageHistory>>()

    fun observeMessages(chatId: String): Flow<ChatMessageHistory> =
        messageHistory(chatId).asStateFlow()

    fun currentHistory(chatId: String): ChatMessageHistory = messageHistory(chatId).value

    fun observedChatIds(): List<String> = messageHistories.keys.toList()

    fun replaceConversations(conversations: List<ChatConversation>) {
        mutableConversations.value = conversations
    }

    fun updateConversations(transform: (List<ChatConversation>) -> List<ChatConversation>) {
        mutableConversations.update(transform)
    }

    fun setTotalUnreadCount(count: Int) {
        mutableTotalUnreadCount.value = count
    }

    fun mergeInitialMessages(
        chatId: String,
        loaded: ChatMessageHistory,
    ) {
        messageHistory(chatId).update { current ->
            loaded.copy(messages = mergeMessages(current.messages, loaded.messages))
        }
    }

    fun mergeOlderMessages(
        chatId: String,
        loaded: ChatMessageHistory,
    ) {
        messageHistory(chatId).update { current ->
            loaded.copy(messages = mergeMessages(loaded.messages, current.messages))
        }
    }

    fun upsertMessage(message: ChatMessage) {
        messageHistory(message.chatId).update { history ->
            history.copy(messages = mergeMessages(history.messages, listOf(message)))
        }
    }

    fun upsertTrackedMessage(message: ChatMessage) {
        messageHistories[message.chatId]?.update { history ->
            history.copy(messages = mergeMessages(history.messages, listOf(message)))
        }
    }

    fun markMessageFailed(message: ChatMessage) {
        messageHistory(message.chatId).update { history ->
            val messages = history.messages.map { existing ->
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

    fun removeConversation(chatId: String) {
        updateConversations { conversations ->
            conversations.filterNot { conversation -> conversation.chatId == chatId }
        }
        messageHistories.remove(chatId)
    }

    fun clear() {
        mutableConversations.value = emptyList()
        mutableTotalUnreadCount.value = 0
        messageHistories.values.forEach { it.value = ChatMessageHistory() }
        messageHistories.clear()
    }

    private fun messageHistory(chatId: String): MutableStateFlow<ChatMessageHistory> =
        messageHistories.getOrPut(chatId) { MutableStateFlow(ChatMessageHistory()) }

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
}
