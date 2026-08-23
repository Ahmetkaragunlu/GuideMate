package com.ahmetkaragunlu.guidemate.payment.data.remote.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

data class CheckoutCurrenciesResponseDto(
    @SerializedName("baseCurrencyCode") val baseCurrencyCode: String,
    @SerializedName("chargeCurrencies")
    val chargeCurrencies: List<CheckoutCurrencyOptionResponseDto>,
)

data class CheckoutCurrencyOptionResponseDto(
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("fractionDigits") val fractionDigits: Int,
)

data class PaymentQuoteResponseDto(
    @SerializedName("quoteId") val quoteId: String,
    @SerializedName("purpose") val purpose: String,
    @SerializedName("baseAmountMinor") val baseAmountMinor: Long,
    @SerializedName("baseCurrencyCode") val baseCurrencyCode: String,
    @SerializedName("chargeAmountMinor") val chargeAmountMinor: Long,
    @SerializedName("chargeCurrencyCode") val chargeCurrencyCode: String,
    @SerializedName("fxRate") val fxRate: BigDecimal,
    @SerializedName("rateSource") val rateSource: String,
    @SerializedName("rateDate") val rateDate: LocalDate,
    @SerializedName("quotedAt") val quotedAt: Instant,
    @SerializedName("expiresAt") val expiresAt: Instant,
)

data class PaymentResponseDto(
    @SerializedName("paymentId") val paymentId: String,
    @SerializedName("purpose") val purpose: String,
    @SerializedName("method") val method: String,
    @SerializedName("paymentStatus") val paymentStatus: String,
    @SerializedName("amountMinor") val amountMinor: Long,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("quoteId") val quoteId: String?,
    @SerializedName("chargeAmountMinor") val chargeAmountMinor: Long?,
    @SerializedName("chargeCurrencyCode") val chargeCurrencyCode: String?,
    @SerializedName("fxRate") val fxRate: BigDecimal?,
    @SerializedName("fxRateSource") val fxRateSource: String?,
    @SerializedName("fxQuotedAt") val fxQuotedAt: Instant?,
    @SerializedName("paymentPageUrl") val paymentPageUrl: String?,
    @SerializedName("expiresAt") val expiresAt: Instant?,
    @SerializedName("reservationId") val reservationId: String?,
    @SerializedName("reservationStatus") val reservationStatus: String?,
    @SerializedName("refundId") val refundId: String?,
    @SerializedName("refundStatus") val refundStatus: String?,
    @SerializedName("refundAmountMinor") val refundAmountMinor: Long?,
    @SerializedName("refundChargeAmountMinor") val refundChargeAmountMinor: Long?,
    @SerializedName("refundChargeCurrencyCode") val refundChargeCurrencyCode: String?,
    @SerializedName("failureCode") val failureCode: String?,
    @SerializedName("createdAt") val createdAt: Instant,
    @SerializedName("updatedAt") val updatedAt: Instant,
)

data class TourPaymentQuoteRequestDto(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("participantCount") val participantCount: Int,
    @SerializedName("chargeCurrencyCode") val chargeCurrencyCode: String,
)

data class TourCheckoutRequestDto(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("participantCount") val participantCount: Int,
    @SerializedName("method") val method: String,
    @SerializedName("quoteId") val quoteId: String?,
    @SerializedName("locale") val locale: String,
)

data class WalletTopUpQuoteRequestDto(
    @SerializedName("amountMinor") val amountMinor: Long,
    @SerializedName("chargeCurrencyCode") val chargeCurrencyCode: String,
)

data class WalletTopUpRequestDto(
    @SerializedName("quoteId") val quoteId: String,
    @SerializedName("locale") val locale: String,
)

