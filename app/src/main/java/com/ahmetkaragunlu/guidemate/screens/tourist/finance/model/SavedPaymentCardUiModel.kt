package com.ahmetkaragunlu.guidemate.screens.tourist.finance.model

import com.ahmetkaragunlu.guidemate.screens.common.moneyaction.model.MoneyActionMethodType
import com.ahmetkaragunlu.guidemate.screens.common.moneyaction.model.MoneyActionMethodUi

data class SavedPaymentCardUiModel(
    val cardId: String,
    val bankName: String,
    val bankCode: Int? = null,
    val cardFamily: String? = null,
    val cardAssociation: PaymentCardAssociation,
    val cardType: PaymentCardType,
    val lastFourDigits: String,
    val cardHolderName: String,
    val expiryMonth: String,
    val expiryYear: String,
    val isDefault: Boolean,
) {
    val maskedCardNumber: String
        get() = "**** **** **** $lastFourDigits"

    val expiryDate: String
        get() = "$expiryMonth/${expiryYear.takeLast(2)}"

    val displayName: String
        get() =
            listOfNotNull(
                bankName,
                cardFamily?.takeIf(String::isNotBlank),
            ).joinToString(separator = " ")
}

fun SavedPaymentCardUiModel.toMoneyActionMethodUi(): MoneyActionMethodUi =
    MoneyActionMethodUi(
        id = cardId,
        title = displayName,
        subtitle = maskedCardNumber,
        type = MoneyActionMethodType.CARD,
    )
