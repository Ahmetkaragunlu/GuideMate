package com.ahmetkaragunlu.guidemate.screens.tourist.wallet.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.store.TouristFinanceStore
import com.ahmetkaragunlu.guidemate.screens.tourist.wallet.transactions.model.TouristWalletTransactionFilter
import com.ahmetkaragunlu.guidemate.screens.tourist.wallet.transactions.model.TouristWalletTransactionsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class TouristWalletTransactionsViewModel
    @Inject
    constructor(
        financeStore: TouristFinanceStore,
    ) : ViewModel() {
        private val selectedFilter = MutableStateFlow(TouristWalletTransactionFilter.ALL)

        val uiState: StateFlow<TouristWalletTransactionsUiState> =
            combine(financeStore.state, selectedFilter) { finance, filter ->
                TouristWalletTransactionsUiState(
                    transactions = finance.transactions.sortedByDescending { it.createdAt },
                    selectedFilter = filter,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    TouristWalletTransactionsUiState(
                        transactions =
                            financeStore.state.value.transactions
                                .sortedByDescending { it.createdAt },
                    ),
            )

        fun selectFilter(filter: TouristWalletTransactionFilter) {
            selectedFilter.value = filter
        }
    }
