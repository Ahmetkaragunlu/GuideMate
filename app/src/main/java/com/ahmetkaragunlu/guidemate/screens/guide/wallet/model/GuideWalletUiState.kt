package com.ahmetkaragunlu.guidemate.screens.guide.wallet.model

import com.ahmetkaragunlu.guidemate.screens.common.model.BankAccount

data class GuideWalletUiState(
    val totalBalance: Double = 0.0,
    val selectedBankAccount: BankAccount? = null,
    val earningSummaries: List<EarningSummary> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList(),
)
