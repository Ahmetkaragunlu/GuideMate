package com.ahmetkaragunlu.guidemate.notification.data.realtime

import com.ahmetkaragunlu.guidemate.common.network.realtime.RealtimeClient
import com.ahmetkaragunlu.guidemate.common.network.realtime.RealtimeDestination
import com.ahmetkaragunlu.guidemate.common.network.realtime.RealtimeEvent
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Singleton
class DefaultNotificationRealtimeClient
@Inject
constructor(
    private val realtimeClient: RealtimeClient,
) : NotificationRealtimeClient {
    override val events: Flow<Unit> =
        realtimeClient.events
            .filter { event ->
                event is RealtimeEvent.Message &&
                    event.destination == RealtimeDestination.NOTIFICATIONS
            }.map {}

    override fun connect() = realtimeClient.connect()

    override fun disconnect() = realtimeClient.disconnect()
}
