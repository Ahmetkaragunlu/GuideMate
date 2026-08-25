package com.ahmetkaragunlu.guidemate.chat.data.realtime

import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatMessageResponseDto
import kotlinx.coroutines.flow.Flow

interface ChatRealtimeClient {
    val events: Flow<ChatRealtimeEvent>

    fun connect()

    fun disconnect()
}

sealed interface ChatRealtimeEvent {
    data object Connected : ChatRealtimeEvent

    data object Disconnected : ChatRealtimeEvent

    data class MessageReceived(
        val message: ChatMessageResponseDto,
    ) : ChatRealtimeEvent

    data class Error(
        val code: String?,
    ) : ChatRealtimeEvent
}
