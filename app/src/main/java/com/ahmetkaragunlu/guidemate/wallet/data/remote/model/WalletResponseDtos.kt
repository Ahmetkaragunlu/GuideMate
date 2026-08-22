package com.ahmetkaragunlu.guidemate.wallet.data.remote.model

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class WalletResponseDto(
    @SerializedName("balanceMinor") val balanceMinor: Long,
    @SerializedName("availableBalanceMinor") val availableBalanceMinor: Long,
    @SerializedName("currencyCode") val currencyCode: String,
)

data class WalletTransactionResponseDto(
    @SerializedName("transactionId") val transactionId: String,
    @SerializedName("direction") val direction: String,
    @SerializedName("type") val type: String,
    @SerializedName("amountMinor") val amountMinor: Long,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("referenceType") val referenceType: String?,
    @SerializedName("referenceId") val referenceId: String?,
    @SerializedName("referenceTitle") val referenceTitle: String?,
    @SerializedName("occurredAt") val occurredAt: Instant,
)
