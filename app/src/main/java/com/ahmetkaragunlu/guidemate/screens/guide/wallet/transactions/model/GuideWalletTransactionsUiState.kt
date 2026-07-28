package com.ahmetkaragunlu.guidemate.screens.guide.wallet.transactions.model

import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.WalletTransactionUiModel

data class GuideWalletTransactionsUiState(
    val transactions: List<WalletTransactionUiModel> = emptyList(),
    val selectedFilter: GuideWalletTransactionFilter = GuideWalletTransactionFilter.ALL,
) {
    val filteredTransactions: List<WalletTransactionUiModel>
        get() =
            selectedFilter.transactionType?.let { selectedType ->
                transactions.filter { transaction -> transaction.type == selectedType }
            } ?: transactions
}
