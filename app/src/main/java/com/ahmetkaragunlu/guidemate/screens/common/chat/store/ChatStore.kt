package com.ahmetkaragunlu.guidemate.screens.common.chat.store

import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatConversation
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatMessage
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatMessageDeliveryStatus
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class ChatStore
@Inject
constructor() {
    private val _conversations = MutableStateFlow(initialChatConversations())
    val conversations: StateFlow<List<ChatConversation>> = _conversations.asStateFlow()

    fun markAsRead(
        chatId: String,
        userId: String,
    ) {
        _conversations.update { conversations ->
            conversations.map { conversation ->
                if (conversation.chatId == chatId && conversation.containsUser(userId)) {
                    conversation.copy(unreadCounts = conversation.unreadCounts + (userId to 0))
                } else {
                    conversation
                }
            }
        }
    }

    fun sendMessage(
        chatId: String,
        senderId: String,
        text: String,
    ) {
        val messageText = text.trim()
        if (messageText.isEmpty()) return

        _conversations.update { conversations ->
            conversations.map { conversation ->
                if (conversation.chatId != chatId || !conversation.containsUser(senderId)) {
                    return@map conversation
                }

                val receiver = conversation.otherParticipant(senderId) ?: return@map conversation
                val clientMessageId = UUID.randomUUID().toString()
                val message =
                    ChatMessage(
                        messageId = clientMessageId,
                        chatId = chatId,
                        senderId = senderId,
                        clientMessageId = clientMessageId,
                        text = messageText,
                        sentAt = Instant.now(),
                        deliveryStatus = ChatMessageDeliveryStatus.SENT,
                    )

                conversation.copy(
                    messages = conversation.messages + message,
                    unreadCounts =
                        conversation.unreadCounts +
                            (receiver.userId to (conversation.unreadCount(receiver.userId) + 1)),
                )
            }
        }
    }
}
