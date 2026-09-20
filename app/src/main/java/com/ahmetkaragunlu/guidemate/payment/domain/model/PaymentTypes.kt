package com.ahmetkaragunlu.guidemate.payment.domain.model

enum class PaymentPurpose {
    WALLET_TOP_UP,
    TOUR_BOOKING,
}

enum class PaymentMethod {
    WALLET,
    HOSTED_CARD,
}

enum class PaymentStatus {
    PENDING,
    REQUIRES_ACTION,
    VERIFYING,
    SUCCEEDED,
    FAILED,
    CANCELLED,
    TIMEOUT,
}

enum class PaymentReservationStatus {
    PENDING_PAYMENT,
    CONFIRMED,
    COMPLETED,
    CANCELLED,
    EXPIRED,
}

enum class PaymentRefundStatus {
    REQUESTED,
    PROCESSING,
    SUCCEEDED,
    FAILED,
    MANUAL_REVIEW,
}
