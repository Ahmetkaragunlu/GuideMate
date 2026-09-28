package com.ahmetkaragunlu.guidemate.wallet.data.remote.model.response

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class GuideEarningResponse(
    @SerializedName("earningId") val earningId: String,
    @SerializedName("reservationId") val reservationId: String,
    @SerializedName("grossMinor") val grossMinor: Long,
    @SerializedName("platformFeeMinor") val platformFeeMinor: Long,
    @SerializedName("netMinor") val netMinor: Long,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("status") val status: String,
    @SerializedName("availableAt") val availableAt: Instant?,
    @SerializedName("createdAt") val createdAt: Instant,
)

data class MonthlyGuideEarningResponse(
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("netEarningsMinor") val netEarningsMinor: Long,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("pendingEarningsMinor") val pendingEarningsMinor: Long,
)

data class BankAccountResponse(
    @SerializedName("bankAccountId") val bankAccountId: String,
    @SerializedName("maskedIban") val maskedIban: String,
    @SerializedName("bankCode") val bankCode: String,
    @SerializedName("bankName") val bankName: String,
    @SerializedName("accountHolderName") val accountHolderName: String,
    @SerializedName("defaultAccount") val defaultAccount: Boolean,
    @SerializedName("createdAt") val createdAt: Instant,
)

data class WithdrawalResponse(
    @SerializedName("withdrawalId") val withdrawalId: String,
    @SerializedName("bankAccountId") val bankAccountId: String,
    @SerializedName("maskedIban") val maskedIban: String,
    @SerializedName("amountMinor") val amountMinor: Long,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("status") val status: String,
    @SerializedName("payoutMode") val payoutMode: String,
    @SerializedName("requestedAt") val requestedAt: Instant,
    @SerializedName("completedAt") val completedAt: Instant?,
    @SerializedName("failureCode") val failureCode: String?,
)
