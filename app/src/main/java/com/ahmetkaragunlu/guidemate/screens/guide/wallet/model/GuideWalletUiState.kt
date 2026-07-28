package com.ahmetkaragunlu.guidemate.screens.guide.wallet.model

import com.ahmetkaragunlu.guidemate.screens.common.moneyaction.model.MoneyActionMethodUi

data class GuideWalletUiState(
    val availableBalanceMinor: Long = 0,
    val selectedBankAccountId: String? = null,
    val defaultMethod: MoneyActionMethodUi? = null,
    val selectedMethod: MoneyActionMethodUi? = null,
    val recentTransactions: List<WalletTransactionUiModel> = emptyList(),
    val isWithdrawalRequestSubmitted: Boolean = false,
)
