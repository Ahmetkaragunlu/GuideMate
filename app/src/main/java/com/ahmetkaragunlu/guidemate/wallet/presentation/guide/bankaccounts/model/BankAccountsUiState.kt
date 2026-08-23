package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class BankAccountsUiState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val bankAccounts: List<BankAccountUiModel> = emptyList(),
    val showDeleteDialogFor: String? = null,
    val showMakeDefaultDialogFor: String? = null,
    val isAddAccountSheetVisible: Boolean = false,
    val addAccountForm: AddBankAccountFormState = AddBankAccountFormState(),
    val isMutationInProgress: Boolean = false,
    val errorMessage: String? = null,
)
