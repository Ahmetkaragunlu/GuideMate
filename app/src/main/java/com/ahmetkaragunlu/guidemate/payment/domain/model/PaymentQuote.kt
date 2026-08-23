package com.ahmetkaragunlu.guidemate.payment.domain.model

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

data class CheckoutCurrencies(
    val baseCurrencyCode: String,
    val chargeCurrencies: List<CheckoutCurrency>,
)

data class CheckoutCurrency(
    val currencyCode: String,
    val fractionDigits: Int,
)

data class PaymentQuote(
    val id: String,
    val purpose: PaymentPurpose,
    val baseAmountMinor: Long,
    val baseCurrencyCode: String,
    val chargeAmountMinor: Long,
    val chargeCurrencyCode: String,
    val fxRate: BigDecimal,
    val rateSource: String,
    val rateDate: LocalDate,
    val quotedAt: Instant,
    val expiresAt: Instant,
) {
    fun isExpired(now: Instant): Boolean = !now.isBefore(expiresAt)
}

enum class CheckoutLocale {
    TR,
    EN,
}

