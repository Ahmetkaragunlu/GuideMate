package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.model.GuideEarningsUiState

@Composable
fun GuideEarningsScreen(
    uiState: GuideEarningsUiState,
    modifier: Modifier = Modifier,
) {
    GuideEarningsContent(
        uiState = uiState,
        modifier = modifier,
    )
}
