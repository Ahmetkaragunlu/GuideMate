package com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.components.AddCardFab
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.components.SavedCardItem
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.model.SavedCardsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedCardsContent(
    uiState: SavedCardsUiState,
    onShowDeleteDialog: (String) -> Unit,
    onDismissDeleteDialog: () -> Unit,
    onConfirmDeleteCard: () -> Unit,
    onShowMakeDefaultDialog: (String) -> Unit,
    onDismissMakeDefaultDialog: () -> Unit,
    onConfirmMakeDefaultCard: () -> Unit,
    onAddCardClick: () -> Unit,
    onDismissAddCardSheet: () -> Unit,
    onCardNumberChange: (String) -> Unit,
    onCardHolderNameChange: (String) -> Unit,
    onExpiryMonthChange: (String) -> Unit,
    onExpiryYearChange: (String) -> Unit,
    onConfirmAddCard: () -> Unit,
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
        }

        AddCardFab(
            onClick = onAddCardClick,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 36.dp, end = 16.dp),
        )
    }

    if (uiState.isAddCardBottomSheetVisible) {
        AddSavedCardBottomSheet(
            sheetState = sheetState,
            formState = uiState.addCardForm,
            onCardNumberChange = onCardNumberChange,
            onCardHolderNameChange = onCardHolderNameChange,
            onExpiryMonthChange = onExpiryMonthChange,
            onExpiryYearChange = onExpiryYearChange,
            onDismiss = onDismissAddCardSheet,
            onConfirm = onConfirmAddCard,
        )
    }

    if (uiState.showDeleteDialogFor != null) {
        EditAlertDialog(
            title = R.string.delete_card_title,
            text = R.string.delete_card_desc,
            confirmButton = {
                TextButton(onClick = onConfirmDeleteCard) {
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
                TextButton(onClick = onConfirmMakeDefaultCard) {
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
