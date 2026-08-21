package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.wallet.data.mock.tourist.TouristWalletStore
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions.model.TouristWalletTransactionFilter
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions.model.TouristWalletTransactionsUiState
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
        walletStore: TouristWalletStore,
    ) : ViewModel() {
        private val selectedFilter = MutableStateFlow(TouristWalletTransactionFilter.ALL)

        val uiState: StateFlow<TouristWalletTransactionsUiState> =
            combine(walletStore.state, selectedFilter) { wallet, filter ->
                TouristWalletTransactionsUiState(
                    transactions = wallet.transactions.sortedByDescending { it.createdAt },
                    selectedFilter = filter,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    TouristWalletTransactionsUiState(
                        transactions =
                            walletStore.state.value.transactions
                                .sortedByDescending { it.createdAt },
                    ),
            )

        fun selectFilter(filter: TouristWalletTransactionFilter) {
            selectedFilter.value = filter
        }
    }
