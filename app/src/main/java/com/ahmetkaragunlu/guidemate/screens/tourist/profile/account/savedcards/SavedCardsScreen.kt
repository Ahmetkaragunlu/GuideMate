package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.viewmodel.TouristSavedCardsViewModel

@Composable
fun SavedCardsScreen(
    showCardAddedMessage: Boolean,
    onCardAddedMessageShown: () -> Unit,
    onNavigateToAddCard: () -> Unit,
    viewModel: TouristSavedCardsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SavedCardsContent(
        uiState = uiState,
        onShowDeleteDialog = viewModel::onShowDeleteDialog,
        onDismissDeleteDialog = viewModel::onDismissDeleteDialog,
        onConfirmDeleteCard = viewModel::onConfirmDeleteCard,
        onShowMakeDefaultDialog = viewModel::onShowMakeDefaultDialog,
        onDismissMakeDefaultDialog = viewModel::onDismissMakeDefaultDialog,
        onConfirmMakeDefaultCard = viewModel::onConfirmMakeDefaultCard,
        onAddCardClick = viewModel::onShowAddCardSheet,
        onDismissAddCardSheet = viewModel::onDismissAddCardSheet,
        onSaveCardConsentChange = viewModel::onSaveCardConsentChange,
        onConfirmAddCard = {
            viewModel.onDismissAddCardSheet()
            onNavigateToAddCard()
        },
        showCardAddedMessage = showCardAddedMessage,
        onCardAddedMessageShown = onCardAddedMessageShown,
    )
}
