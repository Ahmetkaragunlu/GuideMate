package com.ahmetkaragunlu.guidemate.payment.presentation.status.model

import java.time.Instant

data class PaymentAttemptUiModel(
    val paymentAttemptId: String,
    val purpose: PaymentPurpose,
    val amountMinor: Long,
    val currencyCode: String,
    val method: PaymentMethod,
    val status: PaymentAttemptStatus,
    val tourSessionId: String? = null,
    val participantCount: Int? = null,
    val savedCardId: String? = null,
    val createdAt: Instant,
)

enum class PaymentPurpose {
    WALLET_TOP_UP,
    TOUR_BOOKING,
}

enum class PaymentMethod {
    WALLET,
    SAVED_CARD,
}

enum class PaymentAttemptStatus {
    REDIRECTING,
    VERIFYING,
    SUCCEEDED,
    FAILED,
    CANCELLED,
    TIMEOUT,
}
