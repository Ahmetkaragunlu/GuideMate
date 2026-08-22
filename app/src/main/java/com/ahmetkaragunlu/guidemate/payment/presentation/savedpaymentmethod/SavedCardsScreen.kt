package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.viewmodel.TouristSavedCardsViewModel

@Composable
fun SavedCardsScreen(
    viewModel: TouristSavedCardsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::refresh,
    ) {
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
            onConfirmAddCard = viewModel::onDismissAddCardSheet,
            onErrorShown = viewModel::onErrorShown,
        )
    }
}
