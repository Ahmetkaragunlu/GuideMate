package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model.BankAccountsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankAccountsContent(
    uiState: BankAccountsUiState,
    onDeleteClick: (String) -> Unit,
    onDismissDeleteDialog: () -> Unit,
    onConfirmDelete: () -> Unit,
    onMakeDefaultClick: (String) -> Unit,
    onDismissMakeDefaultDialog: () -> Unit,
    onConfirmMakeDefault: () -> Unit,
    onAddClick: () -> Unit,
    onDismissAddSheet: () -> Unit,
    onAccountHolderNameChange: (String) -> Unit,
    onIbanChange: (String) -> Unit,
    onConfirmAdd: () -> Unit,
    onErrorShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = uiState.errorMessage

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            onErrorShown()
            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(
                    horizontal = dimensionResource(R.dimen.spacing_medium),
                    vertical = 12.dp,
                ),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
            contentPadding = PaddingValues(bottom = 80.dp),
        ) {
            item {
                Text(
                    text = stringResource(R.string.bank_accounts_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_color),
                )
            }
            items(
                items = uiState.bankAccounts,
                key = { it.bankAccountId },
            ) { account ->
                BankAccountCard(
                    account = account,
                    onDeleteClick = { onDeleteClick(account.bankAccountId) },
                    onMakeDefaultClick = { onMakeDefaultClick(account.bankAccountId) },
                )
            }

            if (uiState.bankAccounts.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.bank_accounts_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.text_color),
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onAddClick,
            shape = CircleShape,
            containerColor = colorResource(R.color.brand_color),
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        bottom = 36.dp,
                        end = dimensionResource(R.dimen.spacing_medium),
                    ),
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = dimensionResource(R.dimen.spacing_large)),
        )
    }

    if (uiState.isAddAccountSheetVisible) {
        AddBankAccountBottomSheet(
            sheetState = sheetState,
            formState = uiState.addAccountForm,
            onAccountHolderNameChange = onAccountHolderNameChange,
            onIbanChange = onIbanChange,
            onDismiss = onDismissAddSheet,
            onConfirm = onConfirmAdd,
            isSubmitting = uiState.isMutationInProgress,
        )
    }

    if (uiState.showDeleteDialogFor != null) {
        EditAlertDialog(
            title = R.string.delete_bank_account_title,
            text = R.string.delete_bank_account_description,
            confirmButton = {
                TextButton(
                    onClick = onConfirmDelete,
                    enabled = !uiState.isMutationInProgress,
                ) {
                    Text(
                        text = stringResource(R.string.yes),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissDeleteDialog) {
                    Text(
                        text = stringResource(R.string.no),
                        color = colorResource(R.color.text_color),
                    )
                }
            },
            onDismissRequest = onDismissDeleteDialog,
        )
    }

    if (uiState.showMakeDefaultDialogFor != null) {
        EditAlertDialog(
            title = R.string.make_default_bank_account_title,
            text = R.string.make_default_bank_account_description,
            confirmButton = {
                TextButton(
                    onClick = onConfirmMakeDefault,
                    enabled = !uiState.isMutationInProgress,
                ) {
                    Text(
                        text = stringResource(R.string.yes),
                        color = colorResource(R.color.brand_color),
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissMakeDefaultDialog) {
                    Text(
                        text = stringResource(R.string.no),
                        color = colorResource(R.color.text_color),
                    )
                }
            },
            onDismissRequest = onDismissMakeDefaultDialog,
        )
    }
}
