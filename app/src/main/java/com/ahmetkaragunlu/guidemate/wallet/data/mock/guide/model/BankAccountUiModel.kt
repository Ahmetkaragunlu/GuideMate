package com.ahmetkaragunlu.guidemate.wallet.data.mock.guide.model

data class BankAccountUiModel(
    val bankAccountId: String,
    val bankName: String,
    val accountHolderName: String,
    val maskedIban: String,
    val isDefault: Boolean,
)
