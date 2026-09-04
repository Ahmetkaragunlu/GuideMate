package com.ahmetkaragunlu.guidemate.chat.data.realtime

import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatMessageResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatParticipantProfileUpdatedResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatRealtimeErrorResponseDto
import com.ahmetkaragunlu.guidemate.common.network.realtime.RealtimeClient
import com.ahmetkaragunlu.guidemate.common.network.realtime.RealtimeDestination
import com.ahmetkaragunlu.guidemate.common.network.realtime.RealtimeEvent
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

@Singleton
class DefaultChatRealtimeClient
@Inject
constructor(
    private val realtimeClient: RealtimeClient,
    private val gson: Gson,
) : ChatRealtimeClient {
    override val events: Flow<ChatRealtimeEvent> =
        realtimeClient.events.mapNotNull(::toChatEvent)

    override fun connect() = realtimeClient.connect()

    override fun disconnect() = realtimeClient.disconnect()

    private fun toChatEvent(event: RealtimeEvent): ChatRealtimeEvent? =
        when (event) {
            RealtimeEvent.Connected -> ChatRealtimeEvent.Connected
            RealtimeEvent.Disconnected -> ChatRealtimeEvent.Disconnected
            RealtimeEvent.ProtocolError -> ChatRealtimeEvent.Error(code = null)
            is RealtimeEvent.Message -> event.toChatEvent()
        }

    private fun RealtimeEvent.Message.toChatEvent(): ChatRealtimeEvent? =
        runCatching {
            when (destination) {
                RealtimeDestination.CHAT_MESSAGES ->
                    ChatRealtimeEvent.MessageReceived(
                        gson.fromJson(body, ChatMessageResponseDto::class.java),
                    )
                RealtimeDestination.CHAT_PARTICIPANT_UPDATES ->
                    ChatRealtimeEvent.ParticipantProfileUpdated(
                        gson.fromJson(body, ChatParticipantProfileUpdatedResponseDto::class.java),
                    )
                RealtimeDestination.CHAT_ERRORS ->
                    ChatRealtimeEvent.Error(
                        gson.fromJson(body, ChatRealtimeErrorResponseDto::class.java).code,
                    )
                else -> null
            }
        }.getOrNull()
}
