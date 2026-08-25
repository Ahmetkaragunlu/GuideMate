package com.ahmetkaragunlu.guidemate.notification.domain.model

import java.time.Instant

data class AppNotification(
    val notificationId: String,
    val type: NotificationType,
    val actorDisplayName: String?,
    val payload: NotificationPayload,
    val isRead: Boolean,
    val createdAt: Instant,
) {
    val navigationTarget: NotificationNavigationTarget =
        NotificationNavigationTarget(
            notificationId = notificationId,
            type = type,
            chatId = payload.chatId,
            tourId = payload.tourId,
            sessionId = payload.sessionId,
            reservationId = payload.reservationId,
            paymentId = payload.paymentId,
        )
}
