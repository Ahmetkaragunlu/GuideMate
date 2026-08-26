package com.ahmetkaragunlu.guidemate.testing

import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.notification.domain.model.AppNotification
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferenceUpdate
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferences
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
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
    var lastPreferenceUpdate: NotificationPreferenceUpdate? = null
    var clearLocalStateCalls = 0

    override val notifications: StateFlow<List<AppNotification>> = notificationState
    override val unreadCount: StateFlow<Int> = unreadState
    override val preferences: StateFlow<NotificationPreferences?> = preferenceState
    override val hasMoreNotifications: StateFlow<Boolean> = hasMoreState
    override val pushEvents: SharedFlow<NotificationNavigationTarget> = pushEventState

    override suspend fun refreshNotifications(): DataResult<List<AppNotification>> =
        DataResult.Success(notificationState.value)

    override suspend fun loadMoreNotifications(): DataResult<List<AppNotification>> =
        DataResult.Success(emptyList())

    override suspend fun refreshUnreadCount(): DataResult<Int> = DataResult.Success(unreadState.value)

    override suspend fun markRead(notificationId: String): DataResult<AppNotification> =
        error("Not required by this test fixture")

    override suspend fun markAllRead(): DataResult<Int> = DataResult.Success(0)

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

    override suspend fun registerDevice(): DataResult<Unit> = DataResult.Success(Unit)

    override fun onPushReceived(target: NotificationNavigationTarget) = Unit

    override fun clearLocalState() {
        clearLocalStateCalls++
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
