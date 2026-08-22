package com.ahmetkaragunlu.guidemate.wallet.data.mock.guide.model

import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionUiModel

data class GuideWalletState(
    val bankAccounts: List<BankAccountUiModel> = emptyList(),
    val pendingWithdrawals: List<WalletTransactionUiModel> = emptyList(),
) {
    val pendingWithdrawalMinor: Long
        get() = pendingWithdrawals.sumOf { it.amountMinor }

    val defaultBankAccount: BankAccountUiModel?
        get() = bankAccounts.firstOrNull { it.isDefault } ?: bankAccounts.firstOrNull()
}
