package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step2

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step2.content.GuideTourPublishStep2CategoryPriceContent

@Composable
fun GuideTourPublishStep2CategoryPriceScreen(
    uiState: GuideTourPublishUiState,
    onCategoryClick: () -> Unit,
    onAddLanguageClick: () -> Unit,
    onRemoveLanguageClick: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GuideTourPublishStep2CategoryPriceContent(
        uiState = uiState,
        onCategoryClick = onCategoryClick,
        onAddLanguageClick = onAddLanguageClick,
        onRemoveLanguageClick = onRemoveLanguageClick,
        onPriceChange = onPriceChange,
        onNext = onNext,
        modifier = modifier,
    )
}

