package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.model.GuideEarningsUiState

@Composable
fun GuideEarningsScreen(
    uiState: GuideEarningsUiState,
    onRetry: () -> Unit,
    onYearSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    GuideMateContentState(
        state = uiState.loadState,
        onRetry = onRetry,
        errorMessage = uiState.errorMessage,
        modifier = modifier,
    ) {
        GuideEarningsContent(
            uiState = uiState,
            onYearSelected = onYearSelected,
            modifier = modifier,
        )
    }
}
