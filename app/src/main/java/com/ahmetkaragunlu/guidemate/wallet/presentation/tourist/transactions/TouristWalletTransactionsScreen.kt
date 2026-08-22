package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState

@Composable
fun TouristWalletTransactionsScreen(
    modifier: Modifier = Modifier,
    viewModel: TouristWalletTransactionsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::refresh,
        modifier = modifier,
    ) {
        TouristWalletTransactionsContent(
            uiState = uiState,
            onFilterSelected = viewModel::selectFilter,
            onLoadNextPage = viewModel::loadNextPage,
            modifier = modifier,
        )
    }
}
