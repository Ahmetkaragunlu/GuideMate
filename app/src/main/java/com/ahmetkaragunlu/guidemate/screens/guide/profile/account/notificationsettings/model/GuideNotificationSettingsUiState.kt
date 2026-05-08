package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.notificationsettings.model

data class GuideNotificationSettingsUiState(
    val upcomingTourReminders: Boolean = true,
    val touristMessages: Boolean = true,
    val reservationUpdates: Boolean = true,
    val paymentsAndEarnings: Boolean = true,
    val newReviews: Boolean = true,
    val securityAlerts: Boolean = true,
)
