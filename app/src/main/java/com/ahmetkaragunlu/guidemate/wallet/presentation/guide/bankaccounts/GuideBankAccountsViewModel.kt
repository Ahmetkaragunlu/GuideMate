package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.wallet.domain.iban.TurkishBankCatalog
import com.ahmetkaragunlu.guidemate.wallet.domain.iban.TurkishIbanValidator
import com.ahmetkaragunlu.guidemate.wallet.data.mock.guide.GuideWalletStore
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model.AddBankAccountFormState
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model.BankAccountsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class GuideBankAccountsViewModel
    @Inject
    constructor(
        private val walletStore: GuideWalletStore,
        private val ibanValidator: TurkishIbanValidator,
        private val bankCatalog: TurkishBankCatalog,
    ) : ViewModel() {
        private val actionState = MutableStateFlow(BankAccountsUiState())

        val uiState: StateFlow<BankAccountsUiState> =
            combine(walletStore.state, actionState) { wallet, action ->
                action.copy(bankAccounts = wallet.bankAccounts)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    BankAccountsUiState(
                        bankAccounts = walletStore.state.value.bankAccounts,
                    ),
            )

        fun showDeleteDialog(bankAccountId: String) {
            actionState.update { it.copy(showDeleteDialogFor = bankAccountId) }
        }

        fun dismissDeleteDialog() {
            actionState.update { it.copy(showDeleteDialogFor = null) }
        }

        fun confirmDeleteAccount() {
            val bankAccountId = actionState.value.showDeleteDialogFor ?: return
            walletStore.deleteBankAccount(bankAccountId)
            dismissDeleteDialog()
        }

        fun showMakeDefaultDialog(bankAccountId: String) {
            actionState.update { it.copy(showMakeDefaultDialogFor = bankAccountId) }
        }

        fun dismissMakeDefaultDialog() {
            actionState.update { it.copy(showMakeDefaultDialogFor = null) }
        }

        fun confirmMakeDefaultAccount() {
            val bankAccountId = actionState.value.showMakeDefaultDialogFor ?: return
            walletStore.makeDefaultBankAccount(bankAccountId)
            dismissMakeDefaultDialog()
        }

        fun showAddAccountSheet() {
            actionState.update { it.copy(isAddAccountSheetVisible = true) }
        }

        fun dismissAddAccountSheet() {
            actionState.update {
                it.copy(
                    isAddAccountSheetVisible = false,
                    addAccountForm = AddBankAccountFormState(),
                )
            }
        }

        fun onAccountHolderNameChange(value: String) {
            updateForm { copy(accountHolderName = value) }
        }

        fun onIbanChange(value: String) {
            val ibanBody = ibanValidator.sanitizeBody(value)
            val normalizedIban = ibanValidator.toNormalizedIban(ibanBody)
            val bankName = bankCatalog.bankName(ibanValidator.bankCode(normalizedIban))
            updateForm {
                copy(
                    bankName = bankName,
                    ibanBody = ibanBody,
                    isIbanValid = ibanValidator.isValid(normalizedIban),
                )
            }
        }

        fun confirmAddAccount() {
            val form = actionState.value.addAccountForm
            if (!form.canSubmit) return
            val bankName = form.bankName ?: return
            walletStore.addBankAccount(
                bankName = bankName,
                accountHolderName = form.accountHolderName,
                iban = ibanValidator.toNormalizedIban(form.ibanBody),
            )
            dismissAddAccountSheet()
        }

        private fun updateForm(
            transform: AddBankAccountFormState.() -> AddBankAccountFormState,
        ) {
            actionState.update { it.copy(addAccountForm = it.addAccountForm.transform()) }
        }

    }
