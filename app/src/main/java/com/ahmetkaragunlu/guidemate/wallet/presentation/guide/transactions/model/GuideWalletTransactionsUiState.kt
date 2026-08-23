package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.transactions.model

import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionUiModel
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class GuideWalletTransactionsUiState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val errorMessage: String? = null,
    val transactions: List<WalletTransactionUiModel> = emptyList(),
    val page: Int = 0,
    val isLastPage: Boolean = true,
    val isAppending: Boolean = false,
    val selectedFilter: GuideWalletTransactionFilter = GuideWalletTransactionFilter.ALL,
) {
    val filteredTransactions: List<WalletTransactionUiModel>
        get() =
            selectedFilter.transactionType?.let { selectedType ->
                transactions.filter { transaction -> transaction.type == selectedType }
            } ?: transactions
}
