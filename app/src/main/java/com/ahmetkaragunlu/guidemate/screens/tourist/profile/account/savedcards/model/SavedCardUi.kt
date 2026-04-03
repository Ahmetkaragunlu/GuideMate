package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model

import com.ahmetkaragunlu.guidemate.screens.common.model.BankAccount

data class SavedCardUi(
    val cardId: String,
    val bankName: String,
    val cardNumber: String,
    val cardHolderName: String,
    val expiryDate: String,
    val isDefault: Boolean,
)

fun SavedCardUi.toBankAccount(): BankAccount =
    BankAccount(
        id = cardId,
        bankName = bankName,
        maskedIban = cardNumber,
    )
