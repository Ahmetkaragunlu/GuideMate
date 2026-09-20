package com.ahmetkaragunlu.guidemate.wallet.data.remote.model

import com.google.gson.annotations.SerializedName

data class AddBankAccountRequestDto(
    @SerializedName("iban") val iban: String,
    @SerializedName("accountHolderName") val accountHolderName: String,
)

data class WithdrawalRequestDto(
    @SerializedName("bankAccountId") val bankAccountId: String,
    @SerializedName("amountMinor") val amountMinor: Long,
)
