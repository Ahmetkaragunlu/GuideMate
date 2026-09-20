package com.ahmetkaragunlu.guidemate.payment.data.remote.model

import com.google.gson.annotations.SerializedName

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
