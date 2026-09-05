package com.ahmetkaragunlu.guidemate.notification.domain.model

data class NotificationVisibleTarget(
    val type: NotificationTargetType,
    val targetId: String,
) {
    fun matches(target: NotificationNavigationTarget): Boolean =
        when (type) {
            NotificationTargetType.CHAT -> target.chatId == targetId
            NotificationTargetType.TOUR ->
                target.tourId == targetId || target.sessionId == targetId
            NotificationTargetType.RESERVATION -> target.reservationId == targetId
            NotificationTargetType.PAYMENT -> target.paymentId == targetId
        }
}
