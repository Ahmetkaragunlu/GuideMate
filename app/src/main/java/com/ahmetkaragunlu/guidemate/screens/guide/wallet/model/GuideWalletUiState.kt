package com.ahmetkaragunlu.guidemate.screens.guide.wallet.model

import com.ahmetkaragunlu.guidemate.screens.common.moneyaction.model.MoneyActionMethodUi

data class GuideWalletUiState(
    val totalBalance: Double = 0.0,
    val selectedCardId: String? = null,
    val selectedMethod: MoneyActionMethodUi? = null,
    val recentTransactions: List<Transaction> = emptyList(),
)
