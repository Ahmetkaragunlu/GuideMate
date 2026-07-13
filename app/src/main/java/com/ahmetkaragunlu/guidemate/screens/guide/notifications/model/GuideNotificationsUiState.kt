package com.ahmetkaragunlu.guidemate.screens.guide.notifications.model

private const val HOME_NOTIFICATION_PREVIEW_COUNT = 4

data class GuideNotificationsUiState(
    val notifications: List<GuideNotificationUiModel> = emptyList(),
) {
    val unreadCount: Int
        get() = notifications.count { !it.isRead }

    val recentNotifications: List<GuideNotificationUiModel>
        get() = notifications.take(HOME_NOTIFICATION_PREVIEW_COUNT)
}
