package com.ahmetkaragunlu.guidemate.common.network.realtime

import kotlinx.coroutines.flow.Flow

interface RealtimeClient {
    val events: Flow<RealtimeEvent>

    fun connect()

    fun disconnect()
}

sealed interface RealtimeEvent {
    data object Connected : RealtimeEvent

    data object Disconnected : RealtimeEvent

    data class Message(
        val destination: String,
        val body: String,
    ) : RealtimeEvent

    data object ProtocolError : RealtimeEvent
}

object RealtimeDestination {
    const val CHAT_MESSAGES = "/user/queue/chat-messages"
    const val CHAT_PARTICIPANT_UPDATES = "/user/queue/chat-participant-updates"
    const val CHAT_ERRORS = "/user/queue/chat-errors"
    const val NOTIFICATIONS = "/user/queue/notifications"
}
