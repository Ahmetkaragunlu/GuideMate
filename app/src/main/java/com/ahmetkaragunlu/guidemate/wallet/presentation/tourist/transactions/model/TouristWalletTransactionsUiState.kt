package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions.model

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model.TouristWalletTransactionUiModel

data class TouristWalletTransactionsUiState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val transactions: List<TouristWalletTransactionUiModel> = emptyList(),
    val page: Int = 0,
    val isLastPage: Boolean = true,
    val isAppending: Boolean = false,
    val selectedFilter: TouristWalletTransactionFilter = TouristWalletTransactionFilter.ALL,
) {
    val filteredTransactions: List<TouristWalletTransactionUiModel>
        get() =
            selectedFilter.transactionType?.let { selectedType ->
                transactions.filter { transaction -> transaction.type == selectedType }
            } ?: transactions
}
