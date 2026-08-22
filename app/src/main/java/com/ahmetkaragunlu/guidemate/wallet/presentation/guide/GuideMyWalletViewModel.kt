package com.ahmetkaragunlu.guidemate.wallet.presentation.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.wallet.data.mock.guide.GuideWalletStore
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.toMoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.GuideWalletUiState
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.mapper.toGuideUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val GUIDE_WALLET_PREVIEW_SIZE = 3

@HiltViewModel
class GuideMyWalletViewModel
    @Inject
    constructor(
        private val walletRepository: WalletRepository,
        private val walletStore: GuideWalletStore,
    ) : ViewModel() {
        private val walletAccount = MutableStateFlow<WalletAccount?>(null)
        private val canonicalTransactions = MutableStateFlow<List<WalletTransactionUiModel>>(emptyList())
        private val actionState = MutableStateFlow(GuideWalletUiState())

        val uiState: StateFlow<GuideWalletUiState> =
            combine(
                walletAccount,
                canonicalTransactions,
                walletStore.state,
                actionState,
            ) { account, transactions, walletMock, action ->
                val selectedBankAccount =
                    walletMock.bankAccounts.firstOrNull {
                        it.bankAccountId == action.selectedBankAccountId
                    } ?: walletMock.defaultBankAccount
                action.copy(
                    availableBalanceMinor =
                        (account?.availableBalanceMinor.orZero() -
                            walletMock.pendingWithdrawalMinor).coerceAtLeast(0),
                    currencyCode = account?.currencyCode ?: action.currencyCode,
                    selectedBankAccountId = selectedBankAccount?.bankAccountId,
                    defaultMethod = walletMock.defaultBankAccount?.toMoneyActionMethodUi(),
                    selectedMethod = selectedBankAccount?.toMoneyActionMethodUi(),
                    recentTransactions = walletMock.pendingWithdrawals + transactions,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = GuideWalletUiState(),
            )

        init {
            refresh()
        }

        fun refresh() {
            viewModelScope.launch {
                actionState.update { it.copy(loadState = ContentLoadState.LOADING) }
                val wallet = walletRepository.getWallet()
                val transactions =
                    walletRepository.getTransactions(page = 0, size = GUIDE_WALLET_PREVIEW_SIZE)
                if (wallet is DataResult.Error || transactions is DataResult.Error) {
                    actionState.update { it.copy(loadState = ContentLoadState.ERROR) }
                    return@launch
                }
                walletAccount.value = (wallet as DataResult.Success).data
                canonicalTransactions.value =
                    (transactions as DataResult.Success).data.items.mapNotNull {
                        it.toGuideUiModel()
                    }
                actionState.update { it.copy(loadState = ContentLoadState.CONTENT) }
            }
        }

        fun resetSelectedBankAccountToDefault() {
            actionState.update { it.copy(selectedBankAccountId = null) }
        }

        fun selectNextBankAccount() {
            val nextAccount =
                walletStore.nextBankAccount(uiState.value.selectedBankAccountId) ?: return
            actionState.update { it.copy(selectedBankAccountId = nextAccount.bankAccountId) }
        }

        fun requestWithdrawal(amountMinor: Long): Boolean {
            val state = uiState.value
            val selectedBankAccountId = state.selectedBankAccountId ?: return false
            if (
                !walletStore.addPendingWithdrawal(
                    amountMinor = amountMinor,
                    availableBalanceMinor = state.availableBalanceMinor,
                    currencyCode = state.currencyCode,
                    bankAccountId = selectedBankAccountId,
                )
            ) return false

            actionState.update { it.copy(isWithdrawalRequestSubmitted = true) }
            return true
        }

        fun dismissWithdrawalConfirmation() {
            actionState.update { it.copy(isWithdrawalRequestSubmitted = false) }
        }

        private fun Long?.orZero(): Long = this ?: 0L
    }
