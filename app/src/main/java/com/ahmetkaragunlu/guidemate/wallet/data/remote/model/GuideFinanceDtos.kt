package com.ahmetkaragunlu.guidemate.wallet.data.remote.model

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class GuideEarningResponseDto(
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

data class MonthlyGuideEarningResponseDto(
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("netEarningsMinor") val netEarningsMinor: Long,
    @SerializedName("currencyCode") val currencyCode: String,
)

data class BankAccountResponseDto(
    @SerializedName("bankAccountId") val bankAccountId: String,
    @SerializedName("maskedIban") val maskedIban: String,
    @SerializedName("bankCode") val bankCode: String,
    @SerializedName("bankName") val bankName: String,
    @SerializedName("accountHolderName") val accountHolderName: String,
    @SerializedName("defaultAccount") val defaultAccount: Boolean,
    @SerializedName("createdAt") val createdAt: Instant,
)

data class AddBankAccountRequestDto(
    @SerializedName("iban") val iban: String,
    @SerializedName("accountHolderName") val accountHolderName: String,
)

data class WithdrawalResponseDto(
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

data class WithdrawalRequestDto(
    @SerializedName("bankAccountId") val bankAccountId: String,
    @SerializedName("amountMinor") val amountMinor: Long,
)
