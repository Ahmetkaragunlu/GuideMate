package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.savedcards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.SavedCardsContent
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.savedcards.viewmodel.GuideSavedCardsViewModel

@Composable
fun SavedCardsScreen(
    viewModel: GuideSavedCardsViewModel = hiltViewModel(),
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
        onCardNumberChange = viewModel::onCardNumberChange,
        onCardHolderNameChange = viewModel::onCardHolderNameChange,
        onExpiryMonthChange = viewModel::onExpiryMonthChange,
        onExpiryYearChange = viewModel::onExpiryYearChange,
        onConfirmAddCard = viewModel::onConfirmAddCard,
    )
}
