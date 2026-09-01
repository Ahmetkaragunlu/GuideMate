package com.ahmetkaragunlu.guidemate.notification.domain.model

data class NotificationPayload(
    val chatId: String? = null,
    val tourId: String? = null,
    val sessionId: String? = null,
    val reservationId: String? = null,
    val paymentId: String? = null,
    val reviewId: String? = null,
    val withdrawalId: String? = null,
    val tourTitle: String? = null,
    val commentPreview: String? = null,
    val rejectionReason: String? = null,
    val messagePreview: String? = null,
    val rating: Int? = null,
    val amountMinor: Long? = null,
    val currencyCode: String? = null,
    val securityEvent: NotificationSecurityEvent = NotificationSecurityEvent.UNKNOWN,
)
