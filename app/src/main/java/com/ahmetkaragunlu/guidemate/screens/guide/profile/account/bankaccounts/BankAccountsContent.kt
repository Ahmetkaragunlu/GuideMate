 package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.bankaccounts

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.bankaccounts.model.BankAccountsUiState

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
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
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
        }

        FloatingActionButton(
            onClick = onAddClick,
            shape = CircleShape,
            containerColor = colorResource(R.color.brand_color),
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 36.dp, end = 16.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
            )
        }
    }

    if (uiState.isAddAccountSheetVisible) {
        AddBankAccountBottomSheet(
            sheetState = sheetState,
            formState = uiState.addAccountForm,
            onAccountHolderNameChange = onAccountHolderNameChange,
            onIbanChange = onIbanChange,
            onDismiss = onDismissAddSheet,
            onConfirm = onConfirmAdd,
        )
    }

    if (uiState.showDeleteDialogFor != null) {
        EditAlertDialog(
            title = R.string.delete_bank_account_title,
            text = R.string.delete_bank_account_description,
            confirmButton = {
                TextButton(onClick = onConfirmDelete) {
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
                TextButton(onClick = onConfirmMakeDefault) {
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
