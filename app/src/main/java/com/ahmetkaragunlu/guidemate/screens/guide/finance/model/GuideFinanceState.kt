package com.ahmetkaragunlu.guidemate.screens.guide.finance.model

import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.WalletTransactionUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.WalletTransactionStatus
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.WalletTransactionType

data class GuideFinanceState(
    val balanceMinor: Long = 0,
    val bankAccounts: List<BankAccountUiModel> = emptyList(),
    val recentTransactions: List<WalletTransactionUiModel> = emptyList(),
) {
    val availableWithdrawalBalanceMinor: Long
        get() =
            (
                balanceMinor -
                    recentTransactions
                        .filter {
                            it.type == WalletTransactionType.WITHDRAWAL &&
                                it.status == WalletTransactionStatus.PENDING
                        }.sumOf { it.amountMinor }
            ).coerceAtLeast(0)

    val defaultBankAccount: BankAccountUiModel?
        get() = bankAccounts.firstOrNull { it.isDefault } ?: bankAccounts.firstOrNull()
}
