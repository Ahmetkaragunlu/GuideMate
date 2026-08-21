package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.transactions.model

import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionUiModel

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
