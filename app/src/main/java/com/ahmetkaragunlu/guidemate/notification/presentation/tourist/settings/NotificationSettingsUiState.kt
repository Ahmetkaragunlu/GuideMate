package com.ahmetkaragunlu.guidemate.notification.presentation.tourist.settings

data class NotificationSettingsUiState(
    val upcomingReminder: Boolean = true,
    val guideMessages: Boolean = true,
    val reservationUpdates: Boolean = true,
    val reviewRequests: Boolean = true,
    val securityAlerts: Boolean = true,
)
