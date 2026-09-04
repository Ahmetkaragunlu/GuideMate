package com.ahmetkaragunlu.guidemate.chat.domain.repository

import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatConversation
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessage
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageHistory
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ChatRepository {
    val conversations: StateFlow<List<ChatConversation>>
    val totalUnreadCount: StateFlow<Int>

    fun observeMessages(chatId: String): Flow<ChatMessageHistory>

    suspend fun refreshConversations(): DataResult<List<ChatConversation>>

    suspend fun refreshUnreadCount(): DataResult<Int>

    suspend fun loadInitialMessages(chatId: String): DataResult<ChatMessageHistory>

    suspend fun loadOlderMessages(chatId: String): DataResult<ChatMessageHistory>

    suspend fun sendMessage(
        chatId: String,
        text: String,
    ): DataResult<ChatMessage>

    suspend fun retryMessage(
        chatId: String,
        clientMessageId: String,
    ): DataResult<ChatMessage>

    suspend fun markRead(chatId: String): DataResult<Int>

    suspend fun clearConversation(chatId: String): DataResult<Int>

    suspend fun findOrCreate(remoteUserId: Long): DataResult<ChatConversation>
}
