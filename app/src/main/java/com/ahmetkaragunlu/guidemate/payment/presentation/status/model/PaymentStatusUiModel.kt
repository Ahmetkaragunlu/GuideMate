package com.ahmetkaragunlu.guidemate.payment.presentation.status.model

import com.ahmetkaragunlu.guidemate.payment.domain.model.Payment
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentRefundStatus
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentReservationStatus
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentStatus

data class PaymentStatusUiModel(
    val paymentId: String,
    val purpose: PaymentPurpose,
    val amountMinor: Long,
    val currencyCode: String,
    val status: PaymentUiStatus,
)

enum class PaymentUiStatus {
    VERIFYING,
    SUCCEEDED,
    FAILED,
    CANCELLED,
    TIMEOUT,
    REFUND_PENDING,
    REFUNDED,
    MANUAL_REVIEW,
}

internal fun Payment.toStatusUiModel(walletProjectionVerified: Boolean = true): PaymentStatusUiModel =
    PaymentStatusUiModel(
        paymentId = id,
        purpose = purpose,
        amountMinor = amountMinor,
        currencyCode = currencyCode,
        status = resolveUiStatus(walletProjectionVerified),
    )

private fun Payment.resolveUiStatus(walletProjectionVerified: Boolean): PaymentUiStatus =
    when (refundStatus) {
        PaymentRefundStatus.MANUAL_REVIEW -> PaymentUiStatus.MANUAL_REVIEW
        PaymentRefundStatus.REQUESTED,
        PaymentRefundStatus.PROCESSING,
        -> PaymentUiStatus.REFUND_PENDING
        PaymentRefundStatus.SUCCEEDED -> PaymentUiStatus.REFUNDED
        PaymentRefundStatus.FAILED -> PaymentUiStatus.FAILED
        null ->
            when (status) {
                PaymentStatus.PENDING,
                PaymentStatus.REQUIRES_ACTION,
                PaymentStatus.VERIFYING,
                -> PaymentUiStatus.VERIFYING
                PaymentStatus.FAILED -> PaymentUiStatus.FAILED
                PaymentStatus.CANCELLED -> PaymentUiStatus.CANCELLED
                PaymentStatus.TIMEOUT -> PaymentUiStatus.TIMEOUT
                PaymentStatus.SUCCEEDED ->
                    when (purpose) {
                        PaymentPurpose.WALLET_TOP_UP ->
                            if (walletProjectionVerified) {
                                PaymentUiStatus.SUCCEEDED
                            } else {
                                PaymentUiStatus.VERIFYING
                            }
                        PaymentPurpose.TOUR_BOOKING ->
                            if (reservationStatus == PaymentReservationStatus.CONFIRMED) {
                                PaymentUiStatus.SUCCEEDED
                            } else {
                                PaymentUiStatus.VERIFYING
                            }
                    }
            }
    }

