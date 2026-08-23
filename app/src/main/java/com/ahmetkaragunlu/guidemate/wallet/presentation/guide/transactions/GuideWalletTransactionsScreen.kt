package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.transactions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

@Composable
fun GuideWalletTransactionsScreen(
    modifier: Modifier = Modifier,
    viewModel: GuideWalletTransactionsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val paginationError = uiState.errorMessage.takeIf { uiState.loadState == ContentLoadState.CONTENT }

    LaunchedEffect(paginationError) {
        if (paginationError != null) {
            viewModel.clearError()
            snackbarHostState.showSnackbar(paginationError)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GuideMateContentState(
            state = uiState.loadState,
            onRetry = viewModel::refresh,
            modifier = modifier,
            errorMessage = uiState.errorMessage,
        ) {
            GuideWalletTransactionsContent(
                uiState = uiState,
                onFilterSelected = viewModel::selectFilter,
                onLoadNextPage = viewModel::loadNextPage,
                modifier = modifier,
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
