package com.ahmetkaragunlu.guidemate.testing.notification

import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.notification.domain.model.AppNotification
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPayload
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferenceUpdate
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferences
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetReference
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import java.time.Instant
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class NotificationFakeState {
    val notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val unreadCount = MutableStateFlow(0)
    val preferences = MutableStateFlow<NotificationPreferences?>(null)
    val hasMoreNotifications = MutableStateFlow(false)
    val pushEvents = MutableSharedFlow<NotificationNavigationTarget>()
}

class NotificationFakeResults {
    var refreshPreferences: DataResult<NotificationPreferences> =
        DataResult.Success(defaultNotificationPreferences())
    var updatePreferences: DataResult<NotificationPreferences> =
        DataResult.Success(defaultNotificationPreferences())
    var refreshNotifications: DataResult<List<AppNotification>>? = null
    var loadMoreNotifications: DataResult<List<AppNotification>> = DataResult.Success(emptyList())
    var loadMoreNotificationsHandler: (suspend () -> DataResult<List<AppNotification>>)? = null
    var refreshUnreadCount: DataResult<Int>? = null
    var markRead: DataResult<AppNotification>? = null
    var markAllRead: DataResult<Int> = DataResult.Success(0)
    var markRelatedRead: DataResult<Int> = DataResult.Success(0)
}

class NotificationFakeCalls {
    var refreshNotifications = 0
    var loadMoreNotifications = 0
    var refreshUnreadCount = 0
    val markedNotificationIds = mutableListOf<String>()
    var markAllRead = 0
    var lastPreferenceUpdate: NotificationPreferenceUpdate? = null
    val markedRelatedTargets = mutableListOf<NotificationTargetReference>()
    var clearLocalState = 0
    var dismissSystemNotifications = 0
}

class NotificationFakeFailures {
    var dismissSystemNotifications: Throwable? = null
    var clearLocalState: Throwable? = null
}

class FakeNotificationRepository : NotificationRepository {
    val state = NotificationFakeState()
    val results = NotificationFakeResults()
    val calls = NotificationFakeCalls()
    val failures = NotificationFakeFailures()

    override val notifications: StateFlow<List<AppNotification>> = state.notifications
    override val unreadCount: StateFlow<Int> = state.unreadCount
    override val preferences: StateFlow<NotificationPreferences?> = state.preferences
    override val hasMoreNotifications: StateFlow<Boolean> = state.hasMoreNotifications
    override val pushEvents: SharedFlow<NotificationNavigationTarget> = state.pushEvents

    override suspend fun refreshNotifications(): DataResult<List<AppNotification>> {
        calls.refreshNotifications++
        return results.refreshNotifications ?: DataResult.Success(state.notifications.value)
    }

    override suspend fun loadMoreNotifications(): DataResult<List<AppNotification>> {
        calls.loadMoreNotifications++
        return results.loadMoreNotificationsHandler?.invoke() ?: results.loadMoreNotifications
    }

    override suspend fun refreshUnreadCount(): DataResult<Int> {
        calls.refreshUnreadCount++
        return results.refreshUnreadCount ?: DataResult.Success(state.unreadCount.value)
    }

    override suspend fun markRead(notificationId: String): DataResult<AppNotification> {
        calls.markedNotificationIds += notificationId
        val notification =
            results.markRead
                ?: state.notifications.value
                    .first { it.notificationId == notificationId }
                    .copy(isRead = true)
                    .let { DataResult.Success(it) }
        if (notification is DataResult.Success) {
            val wasUnread = state.notifications.value.any {
                it.notificationId == notificationId && !it.isRead
            }
            state.notifications.value =
                state.notifications.value.map { current ->
                    if (current.notificationId == notificationId) notification.data else current
                }
            if (wasUnread) {
                state.unreadCount.value = (state.unreadCount.value - 1).coerceAtLeast(0)
            }
        }
        return notification
    }

    override suspend fun markAllRead(): DataResult<Int> {
        calls.markAllRead++
        when (val result = results.markAllRead) {
            is DataResult.Success -> {
                state.notifications.value = state.notifications.value.map { it.copy(isRead = true) }
                state.unreadCount.value = result.data
            }
            is DataResult.Error -> Unit
        }
        return results.markAllRead
    }

    override suspend fun markRelatedRead(target: NotificationTargetReference): DataResult<Int> {
        calls.markedRelatedTargets += target
        val result = results.markRelatedRead
        if (result is DataResult.Success) state.unreadCount.value = result.data
        return result
    }

    override suspend fun refreshPreferences(): DataResult<NotificationPreferences> {
        val result = results.refreshPreferences
        if (result is DataResult.Success) state.preferences.value = result.data
        return result
    }

    override suspend fun updatePreferences(
        update: NotificationPreferenceUpdate
    ): DataResult<NotificationPreferences> {
        calls.lastPreferenceUpdate = update
        val result = results.updatePreferences
        if (result is DataResult.Success) state.preferences.value = result.data
        return result
    }

    override suspend fun registerDevice(pushInstallationId: String?): DataResult<Unit> =
        DataResult.Success(Unit)

    override fun onPushReceived(target: NotificationNavigationTarget) = Unit

    override fun dismissSystemNotifications() {
        calls.dismissSystemNotifications++
        failures.dismissSystemNotifications?.let { throw it }
    }

    override fun clearLocalState() {
        calls.clearLocalState++
        failures.clearLocalState?.let { throw it }
    }
}

fun defaultNotificationPreferences(
    chatMessagesEnabled: Boolean = true,
): NotificationPreferences =
    NotificationPreferences(
        upcomingTourRemindersEnabled = true,
        chatMessagesEnabled = chatMessagesEnabled,
        reservationUpdatesEnabled = true,
        reviewRequestsEnabled = true,
        paymentsAndEarningsEnabled = true,
        newReviewsEnabled = true,
        securityAlertsEnabled = true,
    )

fun testNotification(
    id: String = "notification-1",
    type: NotificationType = NotificationType.EARNING_AVAILABLE,
    createdAt: Instant = Instant.parse("2026-01-01T00:00:00Z"),
): AppNotification =
    AppNotification(
        notificationId = id,
        type = type,
        actorDisplayName = null,
        payload = NotificationPayload(),
        isRead = false,
        createdAt = createdAt,
    )
