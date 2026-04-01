package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model


data class SavedCardUi(
    val cardId: String,
    val bankName: String,
    val cardNumber: String,
    val cardHolderName: String,
    val expiryDate: String,
    val isDefault: Boolean,
)