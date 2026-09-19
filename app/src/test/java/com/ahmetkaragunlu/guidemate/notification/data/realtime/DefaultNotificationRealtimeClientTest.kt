package com.ahmetkaragunlu.guidemate.notification.data.realtime

import com.ahmetkaragunlu.guidemate.common.network.realtime.RealtimeClient
import com.ahmetkaragunlu.guidemate.common.network.realtime.RealtimeDestination
import com.ahmetkaragunlu.guidemate.common.network.realtime.RealtimeEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultNotificationRealtimeClientTest {
    @Test
    fun `emits only notification destination messages`() = runTest {
        val realtimeClient = FakeRealtimeClient()
        val client = DefaultNotificationRealtimeClient(realtimeClient)
        var receivedEvents = 0
        val collection =
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                client.events.collect { receivedEvents++ }
            }

        realtimeClient.emit(RealtimeEvent.Connected)
        realtimeClient.emit(RealtimeEvent.Message(RealtimeDestination.CHAT_MESSAGES, "chat"))
        realtimeClient.emit(
            RealtimeEvent.Message(RealtimeDestination.NOTIFICATIONS, "notification")
        )
        realtimeClient.emit(RealtimeEvent.ProtocolError)

        assertEquals(1, receivedEvents)
        collection.cancel()
    }

    @Test
    fun `connect and disconnect delegate to shared realtime client`() {
        val realtimeClient = FakeRealtimeClient()
        val client = DefaultNotificationRealtimeClient(realtimeClient)

        client.connect()
        client.disconnect()

        assertEquals(1, realtimeClient.connectCalls)
        assertEquals(1, realtimeClient.disconnectCalls)
    }

    private class FakeRealtimeClient : RealtimeClient {
        private val mutableEvents = MutableSharedFlow<RealtimeEvent>(extraBufferCapacity = 4)
        override val events: Flow<RealtimeEvent> = mutableEvents
        var connectCalls = 0
        var disconnectCalls = 0

        fun emit(event: RealtimeEvent) {
            check(mutableEvents.tryEmit(event))
        }

        override fun connect() {
            connectCalls++
        }

        override fun disconnect() {
            disconnectCalls++
        }
    }
}
