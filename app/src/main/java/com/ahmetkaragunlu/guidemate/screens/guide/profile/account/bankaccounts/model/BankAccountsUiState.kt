package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.bankaccounts.model

import com.ahmetkaragunlu.guidemate.screens.guide.finance.model.BankAccountUiModel

data class BankAccountsUiState(
    val bankAccounts: List<BankAccountUiModel> = emptyList(),
    val showDeleteDialogFor: String? = null,
    val showMakeDefaultDialogFor: String? = null,
    val isAddAccountSheetVisible: Boolean = false,
    val addAccountForm: AddBankAccountFormState = AddBankAccountFormState(),
)
