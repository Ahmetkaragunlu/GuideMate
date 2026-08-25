package com.ahmetkaragunlu.guidemate.notification.domain.model

data class NotificationPreferences(
    val upcomingTourRemindersEnabled: Boolean,
    val chatMessagesEnabled: Boolean,
    val reservationUpdatesEnabled: Boolean,
    val reviewRequestsEnabled: Boolean,
    val paymentsAndEarningsEnabled: Boolean,
    val newReviewsEnabled: Boolean,
    val securityAlertsEnabled: Boolean,
)

data class NotificationPreferenceUpdate(
    val upcomingTourRemindersEnabled: Boolean? = null,
    val chatMessagesEnabled: Boolean? = null,
    val reservationUpdatesEnabled: Boolean? = null,
    val reviewRequestsEnabled: Boolean? = null,
    val paymentsAndEarningsEnabled: Boolean? = null,
    val newReviewsEnabled: Boolean? = null,
)
