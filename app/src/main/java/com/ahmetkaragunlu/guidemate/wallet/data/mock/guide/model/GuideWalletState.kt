package com.ahmetkaragunlu.guidemate.wallet.data.mock.guide.model

import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionStatus
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionType

data class GuideWalletState(
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
