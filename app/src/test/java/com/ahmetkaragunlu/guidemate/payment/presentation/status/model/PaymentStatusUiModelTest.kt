package com.ahmetkaragunlu.guidemate.payment.presentation.status.model

import com.ahmetkaragunlu.guidemate.payment.domain.model.Payment
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentRefundStatus
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentReservationStatus
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentStatus
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Test

class PaymentStatusUiModelTest {
    @Test
    fun `tour payment is successful only with confirmed reservation`() {
        val pendingReservation = payment(reservationStatus = PaymentReservationStatus.PENDING_PAYMENT)
        val confirmedReservation = payment(reservationStatus = PaymentReservationStatus.CONFIRMED)

        assertEquals(PaymentUiStatus.VERIFYING, pendingReservation.toStatusUiModel().status)
        assertEquals(PaymentUiStatus.SUCCEEDED, confirmedReservation.toStatusUiModel().status)
    }

    @Test
    fun `manual review has priority over succeeded provider payment`() {
        val payment =
            payment(
                reservationStatus = PaymentReservationStatus.CANCELLED,
                refundStatus = PaymentRefundStatus.MANUAL_REVIEW,
            )

        assertEquals(PaymentUiStatus.MANUAL_REVIEW, payment.toStatusUiModel().status)
    }

    @Test
    fun `wallet top up waits until wallet projection is refreshed`() {
        val payment =
            payment(
                purpose = PaymentPurpose.WALLET_TOP_UP,
                reservationStatus = null,
            )

        assertEquals(
            PaymentUiStatus.VERIFYING,
            payment.toStatusUiModel(walletProjectionVerified = false).status,
        )
        assertEquals(PaymentUiStatus.SUCCEEDED, payment.toStatusUiModel().status)
    }

    private fun payment(
        purpose: PaymentPurpose = PaymentPurpose.TOUR_BOOKING,
        reservationStatus: PaymentReservationStatus?,
        refundStatus: PaymentRefundStatus? = null,
    ): Payment =
        Payment(
            id = "payment-1",
            purpose = purpose,
            method = PaymentMethod.HOSTED_CARD,
            status = PaymentStatus.SUCCEEDED,
            amountMinor = 10_000,
            currencyCode = "USD",
            quoteId = null,
            chargeAmountMinor = null,
            chargeCurrencyCode = null,
            fxRate = null,
            fxRateSource = null,
            fxQuotedAt = null,
            paymentPageUrl = null,
            expiresAt = null,
            reservationId = null,
            reservationStatus = reservationStatus,
            refundId = null,
            refundStatus = refundStatus,
            refundAmountMinor = null,
            refundChargeAmountMinor = null,
            refundChargeCurrencyCode = null,
            failureCode = null,
            createdAt = Instant.EPOCH,
            updatedAt = Instant.EPOCH,
        )
}

