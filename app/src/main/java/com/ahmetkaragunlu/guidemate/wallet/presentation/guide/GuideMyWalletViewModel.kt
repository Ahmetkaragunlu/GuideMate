package com.ahmetkaragunlu.guidemate.wallet.presentation.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.wallet.data.mock.guide.GuideWalletStore
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.toMoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.GuideWalletUiState
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
        private val walletStore: GuideWalletStore,
    ) : ViewModel() {
        private val actionState = MutableStateFlow(GuideWalletUiState())

        val uiState: StateFlow<GuideWalletUiState> =
            combine(walletStore.state, actionState) { wallet, action ->
                val selectedBankAccount =
                    wallet.bankAccounts.firstOrNull {
                        it.bankAccountId == action.selectedBankAccountId
                } ?: wallet.defaultBankAccount
                action.copy(
                    availableBalanceMinor = wallet.availableWithdrawalBalanceMinor,
                    selectedBankAccountId = selectedBankAccount?.bankAccountId,
                    defaultMethod = wallet.defaultBankAccount?.toMoneyActionMethodUi(),
                    selectedMethod = selectedBankAccount?.toMoneyActionMethodUi(),
                    recentTransactions = wallet.recentTransactions,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    walletStore.state.value.let { wallet ->
                        GuideWalletUiState(
                            availableBalanceMinor = wallet.availableWithdrawalBalanceMinor,
                            selectedBankAccountId = wallet.defaultBankAccount?.bankAccountId,
                            defaultMethod =
                                wallet.defaultBankAccount?.toMoneyActionMethodUi(),
                            selectedMethod =
                                wallet.defaultBankAccount?.toMoneyActionMethodUi(),
                            recentTransactions = wallet.recentTransactions,
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
                walletStore.nextBankAccount(uiState.value.selectedBankAccountId) ?: return
            actionState.update { it.copy(selectedBankAccountId = nextAccount.bankAccountId) }
        }

        fun requestWithdrawal(amountMinor: Long): Boolean {
            val state = uiState.value
            val selectedBankAccountId = state.selectedBankAccountId ?: return false
            if (
                !walletStore.addPendingWithdrawal(
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
