package com.ahmetkaragunlu.guidemate.wallet.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class AddBankAccountRequest(
    @SerializedName("iban") val iban: String,
    @SerializedName("accountHolderName") val accountHolderName: String,
)

data class WithdrawalRequest(
    @SerializedName("bankAccountId") val bankAccountId: String,
    @SerializedName("amountMinor") val amountMinor: Long,
)
