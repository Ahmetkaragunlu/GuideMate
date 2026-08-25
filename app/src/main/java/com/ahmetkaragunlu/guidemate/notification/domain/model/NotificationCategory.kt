package com.ahmetkaragunlu.guidemate.notification.domain.model

enum class NotificationCategory {
    TOUR,
    CHAT,
    COMMENT,
    RATING,
    PAYMENT,
    SECURITY,
    GENERAL,
}

val NotificationType.category: NotificationCategory
    get() =
        when (this) {
            NotificationType.CHAT_MESSAGE -> NotificationCategory.CHAT
            NotificationType.COMMENT_RECEIVED -> NotificationCategory.COMMENT
            NotificationType.RATING_RECEIVED,
            NotificationType.REVIEW_REQUEST,
            -> NotificationCategory.RATING
            NotificationType.PAYMENT_SUCCEEDED,
            NotificationType.PAYMENT_FAILED,
            NotificationType.REFUND_REQUESTED,
            NotificationType.REFUND_COMPLETED,
            NotificationType.REFUND_FAILED,
            NotificationType.REFUND_MANUAL_REVIEW,
            NotificationType.EARNING_AVAILABLE,
            NotificationType.WITHDRAWAL_COMPLETED,
            -> NotificationCategory.PAYMENT
            NotificationType.SECURITY_ALERT -> NotificationCategory.SECURITY
            NotificationType.UNKNOWN -> NotificationCategory.GENERAL
            else -> NotificationCategory.TOUR
        }
