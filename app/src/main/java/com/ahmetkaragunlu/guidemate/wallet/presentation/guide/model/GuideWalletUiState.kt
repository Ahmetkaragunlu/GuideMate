package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model

import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodUi

data class GuideWalletUiState(
    val availableBalanceMinor: Long = 0,
    val selectedBankAccountId: String? = null,
    val defaultMethod: MoneyActionMethodUi? = null,
    val selectedMethod: MoneyActionMethodUi? = null,
    val recentTransactions: List<WalletTransactionUiModel> = emptyList(),
    val isWithdrawalRequestSubmitted: Boolean = false,
)
