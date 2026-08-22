package com.ahmetkaragunlu.guidemate.payment.data.remote.model

import com.google.gson.annotations.SerializedName

data class SavedPaymentMethodResponseDto(
    @SerializedName("savedPaymentMethodId") val savedPaymentMethodId: String,
    @SerializedName("alias") val alias: String?,
    @SerializedName("bankName") val bankName: String?,
    @SerializedName("bankCode") val bankCode: String?,
    @SerializedName("cardFamily") val cardFamily: String?,
    @SerializedName("cardAssociation") val cardAssociation: String?,
    @SerializedName("cardType") val cardType: String?,
    @SerializedName("lastFourDigits") val lastFourDigits: String,
    @SerializedName("cardHolderName") val cardHolderName: String?,
    @SerializedName("expiryMonth") val expiryMonth: Int?,
    @SerializedName("expiryYear") val expiryYear: Int?,
    @SerializedName("defaultMethod") val defaultMethod: Boolean,
)
