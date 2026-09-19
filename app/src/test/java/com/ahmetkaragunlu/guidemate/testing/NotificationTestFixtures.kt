package com.ahmetkaragunlu.guidemate.testing

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

class FakeNotificationRepository : NotificationRepository {
    val notificationState = MutableStateFlow<List<AppNotification>>(emptyList())
    val unreadState = MutableStateFlow(0)
    val preferenceState = MutableStateFlow<NotificationPreferences?>(null)
    val hasMoreState = MutableStateFlow(false)
    val pushEventState = MutableSharedFlow<NotificationNavigationTarget>()

    var refreshPreferencesResult: DataResult<NotificationPreferences> =
        DataResult.Success(defaultNotificationPreferences())
    var updatePreferencesResult: DataResult<NotificationPreferences> =
        DataResult.Success(defaultNotificationPreferences())
    var refreshNotificationsResult: DataResult<List<AppNotification>>? = null
    var loadMoreNotificationsResult: DataResult<List<AppNotification>> =
        DataResult.Success(emptyList())
    var loadMoreNotificationsHandler: (suspend () -> DataResult<List<AppNotification>>)? = null
    var refreshUnreadCountResult: DataResult<Int>? = null
    var markReadResult: DataResult<AppNotification>? = null
    var markAllReadResult: DataResult<Int> = DataResult.Success(0)
    var refreshNotificationsCalls = 0
    var loadMoreNotificationsCalls = 0
    var refreshUnreadCountCalls = 0
    val markedNotificationIds = mutableListOf<String>()
    var markAllReadCalls = 0
    var lastPreferenceUpdate: NotificationPreferenceUpdate? = null
    val markedRelatedTargets = mutableListOf<NotificationTargetReference>()
    var markRelatedResult: DataResult<Int> = DataResult.Success(0)
    var clearLocalStateCalls = 0
    var dismissSystemNotificationsCalls = 0
    var dismissSystemNotificationsException: Throwable? = null
    var clearLocalStateException: Throwable? = null

    override val notifications: StateFlow<List<AppNotification>> = notificationState
    override val unreadCount: StateFlow<Int> = unreadState
    override val preferences: StateFlow<NotificationPreferences?> = preferenceState
    override val hasMoreNotifications: StateFlow<Boolean> = hasMoreState
    override val pushEvents: SharedFlow<NotificationNavigationTarget> = pushEventState

    override suspend fun refreshNotifications(): DataResult<List<AppNotification>> {
        refreshNotificationsCalls++
        return refreshNotificationsResult ?: DataResult.Success(notificationState.value)
    }

    override suspend fun loadMoreNotifications(): DataResult<List<AppNotification>> {
        loadMoreNotificationsCalls++
        return loadMoreNotificationsHandler?.invoke() ?: loadMoreNotificationsResult
    }

    override suspend fun refreshUnreadCount(): DataResult<Int> {
        refreshUnreadCountCalls++
        return refreshUnreadCountResult ?: DataResult.Success(unreadState.value)
    }

    override suspend fun markRead(notificationId: String): DataResult<AppNotification> {
        markedNotificationIds += notificationId
        val notification =
            markReadResult
                ?: notificationState.value
                    .first { it.notificationId == notificationId }
                    .copy(isRead = true)
                    .let { DataResult.Success(it) }
        if (notification is DataResult.Success) {
            val wasUnread = notificationState.value.any {
                it.notificationId == notificationId && !it.isRead
            }
            notificationState.value =
                notificationState.value.map { current ->
                    if (current.notificationId == notificationId) notification.data else current
                }
            if (wasUnread) unreadState.value = (unreadState.value - 1).coerceAtLeast(0)
        }
        return notification
    }

    override suspend fun markAllRead(): DataResult<Int> {
        markAllReadCalls++
        when (val result = markAllReadResult) {
            is DataResult.Success -> {
                notificationState.value = notificationState.value.map { it.copy(isRead = true) }
                unreadState.value = result.data
            }
            is DataResult.Error -> Unit
        }
        return markAllReadResult
    }

    override suspend fun markRelatedRead(target: NotificationTargetReference): DataResult<Int> {
        markedRelatedTargets += target
        val result = markRelatedResult
        if (result is DataResult.Success) unreadState.value = result.data
        return result
    }

    override suspend fun refreshPreferences(): DataResult<NotificationPreferences> {
        val result = refreshPreferencesResult
        if (result is DataResult.Success) preferenceState.value = result.data
        return result
    }

    override suspend fun updatePreferences(
        update: NotificationPreferenceUpdate
    ): DataResult<NotificationPreferences> {
        lastPreferenceUpdate = update
        val result = updatePreferencesResult
        if (result is DataResult.Success) preferenceState.value = result.data
        return result
    }

    override suspend fun registerDevice(pushInstallationId: String?): DataResult<Unit> =
        DataResult.Success(Unit)

    override fun onPushReceived(target: NotificationNavigationTarget) = Unit

    override fun dismissSystemNotifications() {
        dismissSystemNotificationsCalls++
        dismissSystemNotificationsException?.let { throw it }
    }

    override fun clearLocalState() {
        clearLocalStateCalls++
        clearLocalStateException?.let { throw it }
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
