package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.wallet.domain.iban.TurkishBankCatalog
import com.ahmetkaragunlu.guidemate.wallet.domain.iban.TurkishIbanValidator
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.GuideFinanceRepository
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model.AddBankAccountFormState
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model.BankAccountsUiState
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val BANK_ACCOUNT_PAGE_SIZE = 50

@HiltViewModel
class GuideBankAccountsViewModel
    @Inject
    constructor(
        private val repository: GuideFinanceRepository,
        private val ibanValidator: TurkishIbanValidator,
        private val bankCatalog: TurkishBankCatalog,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow(BankAccountsUiState())
        val uiState: StateFlow<BankAccountsUiState> = mutableUiState.asStateFlow()
        private var refreshJob: Job? = null

        init {
            refresh()
        }

        fun refresh() {
            if (refreshJob?.isActive == true) return
            refreshJob =
                viewModelScope.launch {
                    val hasContent = mutableUiState.value.bankAccounts.isNotEmpty()
                    if (!hasContent) {
                        mutableUiState.update {
                            it.copy(loadState = ContentLoadState.LOADING, errorMessage = null)
                        }
                    }
                    when (val result = repository.getBankAccounts(0, BANK_ACCOUNT_PAGE_SIZE)) {
                        is DataResult.Success ->
                            mutableUiState.update {
                                it.copy(
                                    loadState = ContentLoadState.CONTENT,
                                    bankAccounts = result.data.items.map { account -> account.toUiModel() },
                                    errorMessage = null,
                                )
                            }
                        is DataResult.Error ->
                            mutableUiState.update {
                                it.copy(
                                    loadState =
                                        if (hasContent) {
                                            ContentLoadState.CONTENT
                                        } else {
                                            ContentLoadState.ERROR
                                        },
                                    errorMessage = result.error.toMessage(resourceProvider),
                                )
                            }
                    }
                }
        }

        fun showDeleteDialog(bankAccountId: String) {
            mutableUiState.update { it.copy(showDeleteDialogFor = bankAccountId) }
        }

        fun dismissDeleteDialog() {
            mutableUiState.update { it.copy(showDeleteDialogFor = null) }
        }

        fun confirmDeleteAccount() {
            val bankAccountId = mutableUiState.value.showDeleteDialogFor ?: return
            runMutation(
                request = { repository.deleteBankAccount(bankAccountId) },
                onSuccess = {
                    mutableUiState.update { it.copy(showDeleteDialogFor = null) }
                    refresh()
                },
            )
        }

        fun showMakeDefaultDialog(bankAccountId: String) {
            mutableUiState.update { it.copy(showMakeDefaultDialogFor = bankAccountId) }
        }

        fun dismissMakeDefaultDialog() {
            mutableUiState.update { it.copy(showMakeDefaultDialogFor = null) }
        }

        fun confirmMakeDefaultAccount() {
            val bankAccountId = mutableUiState.value.showMakeDefaultDialogFor ?: return
            runMutation(
                request = { repository.makeDefaultBankAccount(bankAccountId) },
                onSuccess = {
                    mutableUiState.update { it.copy(showMakeDefaultDialogFor = null) }
                    refresh()
                },
            )
        }

        fun showAddAccountSheet() {
            mutableUiState.update { it.copy(isAddAccountSheetVisible = true) }
        }

        fun dismissAddAccountSheet() {
            mutableUiState.update {
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
            val form = mutableUiState.value.addAccountForm
            if (!form.canSubmit) return
            runMutation(
                request = {
                    repository.addBankAccount(
                        iban = ibanValidator.toNormalizedIban(form.ibanBody),
                        accountHolderName = form.accountHolderName.trim(),
                    )
                },
                onSuccess = {
                    dismissAddAccountSheet()
                    refresh()
                },
            )
        }

        fun clearError() {
            mutableUiState.update { it.copy(errorMessage = null) }
        }

        private fun runMutation(
            request: suspend () -> DataResult<*>,
            onSuccess: () -> Unit,
        ) {
            if (mutableUiState.value.isMutationInProgress) return
            viewModelScope.launch {
                mutableUiState.update { it.copy(isMutationInProgress = true, errorMessage = null) }
                when (val result = request()) {
                    is DataResult.Success -> onSuccess()
                    is DataResult.Error ->
                        mutableUiState.update {
                            it.copy(errorMessage = result.error.toMessage(resourceProvider))
                        }
                }
                mutableUiState.update { it.copy(isMutationInProgress = false) }
            }
        }

        private fun updateForm(
            transform: AddBankAccountFormState.() -> AddBankAccountFormState,
        ) {
            mutableUiState.update { it.copy(addAccountForm = it.addAccountForm.transform()) }
        }
    }
