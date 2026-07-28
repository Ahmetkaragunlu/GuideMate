package com.ahmetkaragunlu.guidemate.screens.guide.wallet.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.screens.guide.finance.store.GuideFinanceStore
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.transactions.model.GuideWalletTransactionFilter
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.transactions.model.GuideWalletTransactionsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class GuideWalletTransactionsViewModel
    @Inject
    constructor(
        financeStore: GuideFinanceStore,
    ) : ViewModel() {
        private val selectedFilter = MutableStateFlow(GuideWalletTransactionFilter.ALL)

        val uiState: StateFlow<GuideWalletTransactionsUiState> =
            combine(financeStore.state, selectedFilter) { finance, filter ->
                GuideWalletTransactionsUiState(
                    transactions = finance.recentTransactions.sortedByDescending { it.occurredAt },
                    selectedFilter = filter,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    GuideWalletTransactionsUiState(
                        transactions =
                            financeStore.state.value.recentTransactions
                                .sortedByDescending { it.occurredAt },
                    ),
            )

        fun selectFilter(filter: GuideWalletTransactionFilter) {
            selectedFilter.value = filter
        }
    }
