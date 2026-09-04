package com.ahmetkaragunlu.guidemate.notification.data.realtime

import kotlinx.coroutines.flow.Flow

interface NotificationRealtimeClient {
    val events: Flow<Unit>

    fun connect()

    fun disconnect()
}
