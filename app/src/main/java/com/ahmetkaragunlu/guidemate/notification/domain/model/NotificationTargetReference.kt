package com.ahmetkaragunlu.guidemate.notification.domain.model

data class NotificationTargetReference(
    val type: NotificationTargetType,
    val targetId: String,
) {
    fun matches(payload: NotificationPayload): Boolean =
        when (type) {
            NotificationTargetType.CHAT -> payload.chatId == targetId
            NotificationTargetType.TOUR -> payload.tourId == targetId
            NotificationTargetType.RESERVATION -> payload.reservationId == targetId
            NotificationTargetType.PAYMENT -> payload.paymentId == targetId
        }
}

enum class NotificationTargetType {
    CHAT,
    TOUR,
    RESERVATION,
    PAYMENT,
}
