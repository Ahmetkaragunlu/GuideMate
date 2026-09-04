package com.ahmetkaragunlu.guidemate.notification.domain.repository

import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.notification.domain.model.AppNotification
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferenceUpdate
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferences
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetReference
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface NotificationRepository {
    val notifications: StateFlow<List<AppNotification>>
    val unreadCount: StateFlow<Int>
    val preferences: StateFlow<NotificationPreferences?>
    val hasMoreNotifications: StateFlow<Boolean>
    val pushEvents: SharedFlow<NotificationNavigationTarget>

    suspend fun refreshNotifications(): DataResult<List<AppNotification>>
    suspend fun loadMoreNotifications(): DataResult<List<AppNotification>>
    suspend fun refreshUnreadCount(): DataResult<Int>
    suspend fun markRead(notificationId: String): DataResult<AppNotification>
    suspend fun markAllRead(): DataResult<Int>
    suspend fun markRelatedRead(target: NotificationTargetReference): DataResult<Int>
    suspend fun refreshPreferences(): DataResult<NotificationPreferences>
    suspend fun updatePreferences(update: NotificationPreferenceUpdate): DataResult<NotificationPreferences>
    suspend fun registerDevice(pushInstallationId: String? = null): DataResult<Unit>
    fun onPushReceived(target: NotificationNavigationTarget)
    fun clearLocalState()
}
