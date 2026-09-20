package com.ahmetkaragunlu.guidemate.payment.domain.model

import java.time.Instant

data class Payment(
    val id: String,
    val purpose: PaymentPurpose,
    val method: PaymentMethod,
    val status: PaymentStatus,
    val amountMinor: Long,
    val currencyCode: String,
    val chargeDetails: PaymentChargeDetails?,
    val hostedPayment: HostedPaymentDetails?,
    val reservation: PaymentReservation?,
    val refund: PaymentRefund?,
    val failureCode: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
)
