package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import com.ahmetkaragunlu.guidemate.wallet.presentation.mapper.toTouristUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions.model.TouristWalletTransactionFilter
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions.model.TouristWalletTransactionsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TRANSACTION_PAGE_SIZE = 20

@HiltViewModel
class TouristWalletTransactionsViewModel
    @Inject
    constructor(
        private val repository: WalletRepository,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow(TouristWalletTransactionsUiState())
        val uiState: StateFlow<TouristWalletTransactionsUiState> = mutableUiState.asStateFlow()

        init {
            refresh()
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

        fun selectFilter(filter: TouristWalletTransactionFilter) {
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
                    is DataResult.Success ->
                        mutableUiState.update { current ->
                            val incoming = result.data.items.mapNotNull { it.toTouristUiModel() }
                            current.copy(
                                loadState = ContentLoadState.CONTENT,
                                transactions = if (replace) incoming else current.transactions + incoming,
                                page = result.data.page,
                                isLastPage = result.data.isLast,
                                isAppending = false,
                            )
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
    }
