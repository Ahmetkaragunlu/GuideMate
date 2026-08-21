package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.transactions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GuideWalletTransactionsScreen(
    modifier: Modifier = Modifier,
    viewModel: GuideWalletTransactionsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GuideWalletTransactionsContent(
        uiState = uiState,
        onFilterSelected = viewModel::selectFilter,
        modifier = modifier,
    )
}
