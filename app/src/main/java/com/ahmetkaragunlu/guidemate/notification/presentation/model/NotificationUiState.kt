package com.ahmetkaragunlu.guidemate.notification.presentation.model

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

private const val HOME_NOTIFICATION_PREVIEW_COUNT = 4

data class NotificationUiState(
    val notifications: List<NotificationUiModel> = emptyList(),
    val unreadCount: Int = 0,
    val hasMore: Boolean = false,
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val isLoadingMore: Boolean = false,
    val isMarkingAllRead: Boolean = false,
    val errorMessage: String? = null,
) {
    val recentNotifications: List<NotificationUiModel>
        get() = notifications.take(HOME_NOTIFICATION_PREVIEW_COUNT)
}
