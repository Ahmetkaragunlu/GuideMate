package com.ahmetkaragunlu.guidemate.payment.domain.model

import java.math.BigDecimal
import java.time.Instant

data class Payment(
    val id: String,
    val purpose: PaymentPurpose,
    val method: PaymentMethod,
    val status: PaymentStatus,
    val amountMinor: Long,
    val currencyCode: String,
    val quoteId: String?,
    val chargeAmountMinor: Long?,
    val chargeCurrencyCode: String?,
    val fxRate: BigDecimal?,
    val fxRateSource: String?,
    val fxQuotedAt: Instant?,
    val paymentPageUrl: String?,
    val expiresAt: Instant?,
    val reservationId: String?,
    val reservationStatus: PaymentReservationStatus?,
    val refundId: String?,
    val refundStatus: PaymentRefundStatus?,
    val refundAmountMinor: Long?,
    val refundChargeAmountMinor: Long?,
    val refundChargeCurrencyCode: String?,
    val failureCode: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
)

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

