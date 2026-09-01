package com.ahmetkaragunlu.guidemate.notification.domain.model

data class NotificationNavigationTarget(
    val notificationId: String?,
    val type: NotificationType,
    val chatId: String? = null,
    val tourId: String? = null,
    val sessionId: String? = null,
    val reservationId: String? = null,
    val paymentId: String? = null,
    val securityEvent: NotificationSecurityEvent = NotificationSecurityEvent.UNKNOWN,
)
