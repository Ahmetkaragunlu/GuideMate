package com.ahmetkaragunlu.guidemate.screens.guide.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.screens.guide.finance.model.toMoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.screens.guide.finance.store.GuideFinanceStore
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.GuideWalletUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class GuideMyWalletViewModel
    @Inject
    constructor(
        private val financeStore: GuideFinanceStore,
    ) : ViewModel() {
        private val actionState = MutableStateFlow(GuideWalletUiState())

        val uiState: StateFlow<GuideWalletUiState> =
            combine(financeStore.state, actionState) { finance, action ->
                val selectedBankAccount =
                    finance.bankAccounts.firstOrNull {
                        it.bankAccountId == action.selectedBankAccountId
                } ?: finance.defaultBankAccount
                action.copy(
                    availableBalanceMinor = finance.availableWithdrawalBalanceMinor,
                    selectedBankAccountId = selectedBankAccount?.bankAccountId,
                    defaultMethod = finance.defaultBankAccount?.toMoneyActionMethodUi(),
                    selectedMethod = selectedBankAccount?.toMoneyActionMethodUi(),
                    recentTransactions = finance.recentTransactions,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    financeStore.state.value.let { finance ->
                        GuideWalletUiState(
                            availableBalanceMinor = finance.availableWithdrawalBalanceMinor,
                            selectedBankAccountId = finance.defaultBankAccount?.bankAccountId,
                            defaultMethod =
                                finance.defaultBankAccount?.toMoneyActionMethodUi(),
                            selectedMethod =
                                finance.defaultBankAccount?.toMoneyActionMethodUi(),
                            recentTransactions = finance.recentTransactions,
                        )
                    },
            )

        fun resetSelectedBankAccountToDefault() {
            actionState.update {
                it.copy(selectedBankAccountId = null)
            }
        }

        fun selectNextBankAccount() {
            val nextAccount =
                financeStore.nextBankAccount(uiState.value.selectedBankAccountId) ?: return
            actionState.update { it.copy(selectedBankAccountId = nextAccount.bankAccountId) }
        }

        fun requestWithdrawal(amountMinor: Long): Boolean {
            val state = uiState.value
            val selectedBankAccountId = state.selectedBankAccountId ?: return false
            if (
                !financeStore.addPendingWithdrawal(
                    amountMinor = amountMinor,
                    bankAccountId = selectedBankAccountId,
                )
            ) return false

            actionState.update { it.copy(isWithdrawalRequestSubmitted = true) }
            return true
        }

        fun dismissWithdrawalConfirmation() {
            actionState.update { it.copy(isWithdrawalRequestSubmitted = false) }
        }
    }
