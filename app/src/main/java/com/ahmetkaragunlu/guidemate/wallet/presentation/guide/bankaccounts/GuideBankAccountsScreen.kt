package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState

@Composable
fun GuideBankAccountsScreen(
    viewModel: GuideBankAccountsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::refresh,
        errorMessage = uiState.errorMessage,
    ) {
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
            onErrorShown = viewModel::clearError,
        )
    }
}
