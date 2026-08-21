package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model

import com.ahmetkaragunlu.guidemate.wallet.data.mock.guide.model.BankAccountUiModel

data class BankAccountsUiState(
    val bankAccounts: List<BankAccountUiModel> = emptyList(),
    val showDeleteDialogFor: String? = null,
    val showMakeDefaultDialogFor: String? = null,
    val isAddAccountSheetVisible: Boolean = false,
    val addAccountForm: AddBankAccountFormState = AddBankAccountFormState(),
)
