package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model

import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class GuideWalletUiState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val availableBalanceMinor: Long = 0,
    val currencyCode: String = "USD",
    val selectedBankAccountId: String? = null,
    val defaultMethod: MoneyActionMethodUi? = null,
    val selectedMethod: MoneyActionMethodUi? = null,
    val recentTransactions: List<WalletTransactionUiModel> = emptyList(),
    val isWithdrawalInProgress: Boolean = false,
    val isWithdrawalRequestSubmitted: Boolean = false,
    val errorMessage: String? = null,
    val actionErrorMessage: String? = null,
)
