package com.ahmetkaragunlu.guidemate.chat.data.realtime

import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatMessageResponse
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatParticipantProfileUpdatedResponse
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
        val message: ChatMessageResponse,
    ) : ChatRealtimeEvent

    data class ParticipantProfileUpdated(
        val participant: ChatParticipantProfileUpdatedResponse,
    ) : ChatRealtimeEvent

    data class Error(
        val code: String?,
    ) : ChatRealtimeEvent
}
