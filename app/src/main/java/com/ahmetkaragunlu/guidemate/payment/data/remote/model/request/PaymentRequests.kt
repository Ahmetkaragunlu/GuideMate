package com.ahmetkaragunlu.guidemate.payment.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class TourPaymentQuoteRequest(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("participantCount") val participantCount: Int,
    @SerializedName("chargeCurrencyCode") val chargeCurrencyCode: String,
)

data class TourCheckoutRequest(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("participantCount") val participantCount: Int,
    @SerializedName("method") val method: String,
    @SerializedName("quoteId") val quoteId: String?,
    @SerializedName("locale") val locale: String,
)

data class WalletTopUpQuoteRequest(
    @SerializedName("amountMinor") val amountMinor: Long,
    @SerializedName("chargeCurrencyCode") val chargeCurrencyCode: String,
)

data class WalletTopUpRequest(
    @SerializedName("quoteId") val quoteId: String,
    @SerializedName("locale") val locale: String,
)
