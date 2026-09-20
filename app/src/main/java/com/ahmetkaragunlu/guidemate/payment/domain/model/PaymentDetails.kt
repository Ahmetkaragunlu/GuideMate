package com.ahmetkaragunlu.guidemate.payment.domain.model

import java.math.BigDecimal
import java.time.Instant

data class PaymentChargeDetails(
    val quoteId: String?,
    val amountMinor: Long?,
    val currencyCode: String?,
    val fxRate: BigDecimal?,
    val fxRateSource: String?,
    val fxQuotedAt: Instant?,
)

data class HostedPaymentDetails(
    val pageUrl: String?,
    val expiresAt: Instant?,
)

data class PaymentReservation(
    val id: String?,
    val status: PaymentReservationStatus?,
)

data class PaymentRefund(
    val id: String?,
    val status: PaymentRefundStatus?,
    val amountMinor: Long?,
    val chargeAmountMinor: Long?,
    val chargeCurrencyCode: String?,
)
