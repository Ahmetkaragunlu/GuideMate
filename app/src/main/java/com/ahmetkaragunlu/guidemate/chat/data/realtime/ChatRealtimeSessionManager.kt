package com.ahmetkaragunlu.guidemate.chat.data.realtime

import com.ahmetkaragunlu.guidemate.common.coroutines.ApplicationScope
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val MAX_RECONNECT_DELAY_SECONDS = 30L

@Singleton
class ChatRealtimeSessionManager
@Inject
constructor(
    private val realtimeClient: ChatRealtimeClient,
    @param:ApplicationScope private val applicationScope: CoroutineScope,
) {
    @Volatile private var activeSession: ChatSessionSnapshot? = null
    private var reconnectAttempt = 0
    private var reconnectJob: Job? = null
    private var realtimeEventsJob: Job? = null

    internal fun start(
        session: ChatSessionSnapshot,
        onEvent: suspend (ChatRealtimeEvent) -> Unit,
        onReconnect: suspend () -> Unit,
    ) {
        stop()
        activeSession = session
        realtimeEventsJob =
            applicationScope.launch {
                realtimeClient.events.collectLatest { event ->
                    if (!isCurrent(session)) return@collectLatest
                    when (event) {
                        ChatRealtimeEvent.Connected -> {
                            reconnectAttempt = 0
                            reconnectJob?.cancel()
                            onEvent(event)
                        }
                        ChatRealtimeEvent.Disconnected -> {
                            scheduleReconnect(session, onReconnect)
                        }
                        else -> onEvent(event)
                    }
                }
            }
    }

    fun connect() {
        if (activeSession != null) realtimeClient.connect()
    }

    fun stop() {
        activeSession = null
        reconnectJob?.cancel()
        realtimeEventsJob?.cancel()
        reconnectAttempt = 0
        realtimeClient.disconnect()
    }

    private fun scheduleReconnect(
        session: ChatSessionSnapshot,
        onReconnect: suspend () -> Unit,
    ) {
        if (!isCurrent(session) || reconnectJob?.isActive == true) return
        reconnectJob =
            applicationScope.launch {
                val delaySeconds =
                    min(1L shl reconnectAttempt.coerceAtMost(5), MAX_RECONNECT_DELAY_SECONDS)
                reconnectAttempt++
                delay(delaySeconds * 1_000)
                if (!isCurrent(session)) return@launch
                onReconnect()
                if (isCurrent(session)) realtimeClient.connect()
            }
    }

    private fun isCurrent(session: ChatSessionSnapshot): Boolean = activeSession == session
}

internal data class ChatSessionSnapshot(
    val userId: Long?,
    val generation: Long,
)
