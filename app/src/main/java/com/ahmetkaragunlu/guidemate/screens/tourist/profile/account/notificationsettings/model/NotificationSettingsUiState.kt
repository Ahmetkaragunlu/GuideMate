package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.notificationsettings.model

data class NotificationSettingsUiState(
    val upcomingReminder: Boolean = true,
    val guideMessages: Boolean = true,
    val reservationUpdates: Boolean = true,
    val reviewRequests: Boolean = true,
    val securityAlerts: Boolean = true,
)
