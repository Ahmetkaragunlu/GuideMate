package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model

import com.ahmetkaragunlu.guidemate.wallet.domain.model.BankAccount

data class BankAccountUiModel(
    val bankAccountId: String,
    val bankName: String,
    val accountHolderName: String,
    val maskedIban: String,
    val isDefault: Boolean,
)

fun BankAccount.toUiModel(): BankAccountUiModel =
    BankAccountUiModel(
        bankAccountId = id,
        bankName = bankName,
        accountHolderName = accountHolderName,
        maskedIban = maskedIban,
        isDefault = isDefault,
    )
