package com.ahmetkaragunlu.guidemate.screens.guide.earnings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.content.GuideEarningsContent
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.model.GuideEarningsUiState

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
