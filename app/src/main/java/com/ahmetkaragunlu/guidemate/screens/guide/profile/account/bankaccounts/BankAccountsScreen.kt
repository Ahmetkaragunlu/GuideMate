package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.bankaccounts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BankAccountsScreen(
    viewModel: GuideBankAccountsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BankAccountsContent(
        uiState = uiState,
        onDeleteClick = viewModel::showDeleteDialog,
        onDismissDeleteDialog = viewModel::dismissDeleteDialog,
        onConfirmDelete = viewModel::confirmDeleteAccount,
        onMakeDefaultClick = viewModel::showMakeDefaultDialog,
        onDismissMakeDefaultDialog = viewModel::dismissMakeDefaultDialog,
        onConfirmMakeDefault = viewModel::confirmMakeDefaultAccount,
        onAddClick = viewModel::showAddAccountSheet,
        onDismissAddSheet = viewModel::dismissAddAccountSheet,
        onAccountHolderNameChange = viewModel::onAccountHolderNameChange,
        onIbanChange = viewModel::onIbanChange,
        onConfirmAdd = viewModel::confirmAddAccount,
    )
}
