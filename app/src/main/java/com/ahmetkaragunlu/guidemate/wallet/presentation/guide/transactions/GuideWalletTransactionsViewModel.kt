package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.wallet.data.mock.guide.GuideWalletStore
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.transactions.model.GuideWalletTransactionFilter
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.transactions.model.GuideWalletTransactionsUiState
import com.ahmetkaragunlu.guidemate.wallet.presentation.mapper.toGuideUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TRANSACTION_PAGE_SIZE = 20

@HiltViewModel
class GuideWalletTransactionsViewModel
    @Inject
    constructor(
        private val repository: WalletRepository,
        private val walletStore: GuideWalletStore,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow(GuideWalletTransactionsUiState())
        val uiState: StateFlow<GuideWalletTransactionsUiState> = mutableUiState.asStateFlow()
        private var canonicalTransactions: List<WalletTransactionUiModel> = emptyList()

        init {
            refresh()
            viewModelScope.launch {
                walletStore.state.collect { updateVisibleTransactions() }
            }
        }

        fun refresh() {
            loadPage(page = 0, replace = true)
        }

        fun loadNextPage() {
            val state = mutableUiState.value
            if (state.isLastPage || state.isAppending || state.loadState != ContentLoadState.CONTENT) {
                return
            }
            loadPage(page = state.page + 1, replace = false)
        }

        fun selectFilter(filter: GuideWalletTransactionFilter) {
            mutableUiState.update { it.copy(selectedFilter = filter) }
        }

        private fun loadPage(
            page: Int,
            replace: Boolean,
        ) {
            viewModelScope.launch {
                mutableUiState.update {
                    if (replace) {
                        it.copy(loadState = ContentLoadState.LOADING)
                    } else {
                        it.copy(isAppending = true)
                    }
                }
                when (val result = repository.getTransactions(page, TRANSACTION_PAGE_SIZE)) {
                    is DataResult.Success -> {
                        val incoming = result.data.items.mapNotNull { it.toGuideUiModel() }
                        canonicalTransactions =
                            if (replace) incoming else canonicalTransactions + incoming
                        mutableUiState.update {
                            it.copy(
                                loadState = ContentLoadState.CONTENT,
                                page = result.data.page,
                                isLastPage = result.data.isLast,
                                isAppending = false,
                            )
                        }
                        updateVisibleTransactions()
                    }
                    is DataResult.Error ->
                        mutableUiState.update {
                            it.copy(
                                loadState =
                                    if (replace) ContentLoadState.ERROR else ContentLoadState.CONTENT,
                                isAppending = false,
                            )
                        }
                }
            }
        }

        private fun updateVisibleTransactions() {
            mutableUiState.update {
                it.copy(
                    transactions =
                        (walletStore.state.value.pendingWithdrawals + canonicalTransactions)
                            .sortedByDescending { transaction -> transaction.occurredAt },
                )
            }
        }
    }
