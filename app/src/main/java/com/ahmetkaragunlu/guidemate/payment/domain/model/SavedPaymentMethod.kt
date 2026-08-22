package com.ahmetkaragunlu.guidemate.payment.domain.model

data class SavedPaymentMethod(
    val id: String,
    val alias: String?,
    val bankName: String?,
    val bankCode: String?,
    val cardFamily: String?,
    val cardAssociation: String?,
    val cardType: String?,
    val lastFourDigits: String,
    val cardHolderName: String?,
    val expiryMonth: Int?,
    val expiryYear: Int?,
    val isDefault: Boolean,
)
