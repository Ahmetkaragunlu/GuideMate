package com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.model

import com.ahmetkaragunlu.guidemate.screens.common.moneyaction.model.MoneyActionMethodUi

data class SavedCardUi(
    val cardId: String,
    val bankName: String,
    val cardNumber: String,
    val cardHolderName: String,
    val expiryDate: String,
    val isDefault: Boolean,
)

fun SavedCardUi.toMoneyActionMethodUi(): MoneyActionMethodUi =
    MoneyActionMethodUi(
        id = cardId,
        title = bankName,
        subtitle = cardNumber,
    )
