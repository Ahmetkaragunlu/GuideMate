package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TouristWalletTransactionsScreen(
    modifier: Modifier = Modifier,
    viewModel: TouristWalletTransactionsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TouristWalletTransactionsContent(
        uiState = uiState,
        onFilterSelected = viewModel::selectFilter,
        modifier = modifier,
    )
}
