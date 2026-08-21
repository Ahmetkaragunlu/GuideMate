package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TouristWalletScreen(
    onNavigateToSavedCards: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToPayment: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TouristWalletViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showTopUpSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(showTopUpSheet) {
        if (showTopUpSheet) viewModel.resetSelectedCardToDefault()
    }

    TouristWalletContent(
        uiState = uiState,
        onAddMoneyClick = { showTopUpSheet = true },
        onManageCardsClick = onNavigateToSavedCards,
        onViewAllTransactionsClick = onNavigateToTransactions,
        modifier = modifier,
    )

    if (showTopUpSheet) {
        AddMoneyBottomSheet(
            sheetState = sheetState,
            uiState = uiState,
            onAmountChange = viewModel::onTopUpAmountChange,
            onPresetAmountClick = viewModel::onTopUpPresetSelected,
            onChangeCardClick = viewModel::selectNextCard,
            onDismiss = { showTopUpSheet = false },
            onConfirm = { amountMinor ->
                viewModel.createTopUpAttempt(amountMinor)?.let { paymentAttemptId ->
                    showTopUpSheet = false
                    onNavigateToPayment(paymentAttemptId)
                }
            },
        )
    }
}
