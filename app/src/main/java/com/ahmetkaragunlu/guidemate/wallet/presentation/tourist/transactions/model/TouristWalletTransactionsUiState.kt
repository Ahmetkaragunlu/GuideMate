package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions.model

import com.ahmetkaragunlu.guidemate.wallet.data.mock.tourist.model.TouristWalletTransactionUiModel

data class TouristWalletTransactionsUiState(
    val transactions: List<TouristWalletTransactionUiModel> = emptyList(),
    val selectedFilter: TouristWalletTransactionFilter = TouristWalletTransactionFilter.ALL,
) {
    val filteredTransactions: List<TouristWalletTransactionUiModel>
        get() =
            selectedFilter.transactionType?.let { selectedType ->
                transactions.filter { transaction -> transaction.type == selectedType }
            } ?: transactions
}
