package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.components.SavedCardItem
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.model.SavedCardsUiState

@Composable
fun SavedCardsContent(
    uiState: SavedCardsUiState,
    onShowDeleteDialog: (String) -> Unit,
    onDismissDeleteDialog: () -> Unit,
    onConfirmDeleteCard: () -> Unit,
    onShowMakeDefaultDialog: (String) -> Unit,
    onDismissMakeDefaultDialog: () -> Unit,
    onConfirmMakeDefaultCard: () -> Unit,
    onErrorShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                    text = stringResource(R.string.saved_cards_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_color),
                )
            }

            items(
                items = uiState.savedCards,
                key = { it.cardId },
            ) { card ->
                SavedCardItem(
                    card = card,
                    onDeleteClick = { onShowDeleteDialog(card.cardId) },
                    onMakeDefaultClick = { onShowMakeDefaultDialog(card.cardId) },
                )
            }

            if (uiState.savedCards.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_saved_card),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.text_color),
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = dimensionResource(R.dimen.spacing_large)),
        )
    }

    if (uiState.showDeleteDialogFor != null) {
        EditAlertDialog(
            title = R.string.delete_card_title,
            text = R.string.delete_card_desc,
            confirmButton = {
                TextButton(
                    onClick = onConfirmDeleteCard,
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
                        color = Color.Gray,
                    )
                }
            },
            onDismissRequest = onDismissDeleteDialog,
        )
    }

    if (uiState.showMakeDefaultDialogFor != null) {
        EditAlertDialog(
            title = R.string.make_default_title,
            text = R.string.make_default_desc,
            confirmButton = {
                TextButton(
                    onClick = onConfirmMakeDefaultCard,
                    enabled = !uiState.isMutationInProgress,
                ) {
                    Text(
                        text = stringResource(R.string.yes),
                        color = colorResource(id = R.color.brand_color),
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissMakeDefaultDialog) {
                    Text(
                        text = stringResource(R.string.no),
                        color = Color.Gray,
                    )
                }
            },
            onDismissRequest = onDismissMakeDefaultDialog,
        )
    }
}
