package com.ahmetkaragunlu.guidemate.payment.presentation.model

data class SavedPaymentCardUiModel(
    val cardId: String,
    val bankName: String,
    val bankCode: String? = null,
    val cardFamily: String? = null,
    val cardAssociation: PaymentCardAssociation?,
    val cardType: PaymentCardType?,
    val lastFourDigits: String,
    val cardHolderName: String?,
    val expiryMonth: Int?,
    val expiryYear: Int?,
    val isDefault: Boolean,
) {
    val maskedCardNumber: String
        get() = "**** **** **** $lastFourDigits"

    val expiryDate: String?
        get() =
            if (expiryMonth != null && expiryYear != null) {
                "%02d/%02d".format(expiryMonth, expiryYear % 100)
            } else {
                null
            }

    val displayName: String
        get() =
            listOfNotNull(
                bankName.takeIf(String::isNotBlank),
                cardFamily?.takeIf(String::isNotBlank),
                cardAssociation?.displayName,
            ).distinct().joinToString(separator = " ")
}
