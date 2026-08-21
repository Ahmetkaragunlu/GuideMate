package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.wallet.data.mock.guide.GuideWalletStore
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.transactions.model.GuideWalletTransactionFilter
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.transactions.model.GuideWalletTransactionsUiState
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
        walletStore: GuideWalletStore,
    ) : ViewModel() {
        private val selectedFilter = MutableStateFlow(GuideWalletTransactionFilter.ALL)

        val uiState: StateFlow<GuideWalletTransactionsUiState> =
            combine(walletStore.state, selectedFilter) { wallet, filter ->
                GuideWalletTransactionsUiState(
                    transactions = wallet.recentTransactions.sortedByDescending { it.occurredAt },
                    selectedFilter = filter,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    GuideWalletTransactionsUiState(
                        transactions =
                            walletStore.state.value.recentTransactions
                                .sortedByDescending { it.occurredAt },
                    ),
            )

        fun selectFilter(filter: GuideWalletTransactionFilter) {
            selectedFilter.value = filter
        }
    }
