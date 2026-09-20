package com.ahmetkaragunlu.guidemate.notification.domain.model

enum class NotificationType {
    // Tour, reservation, and review lifecycle
    TOUR_APPROVED,
    TOUR_REJECTED,
    TOUR_CHANGE_APPROVED,
    TOUR_CHANGE_REJECTED,
    TOUR_PURCHASED,
    RESERVATION_CONFIRMED,
    RESERVATION_CANCELLED,
    TOUR_CANCELLED,
    TOUR_COMPLETED,
    REVIEW_REQUEST,
    RATING_RECEIVED,
    COMMENT_RECEIVED,

    // Payments, refunds, and guide earnings
    PAYMENT_SUCCEEDED,
    PAYMENT_FAILED,
    REFUND_REQUESTED,
    REFUND_COMPLETED,
    REFUND_FAILED,
    REFUND_MANUAL_REVIEW,
    EARNING_AVAILABLE,
    WITHDRAWAL_COMPLETED,

    // Communication, reminders, and security
    CHAT_MESSAGE,
    UPCOMING_TOUR_REMINDER,
    SECURITY_ALERT,

    UNKNOWN,
    ;
}
