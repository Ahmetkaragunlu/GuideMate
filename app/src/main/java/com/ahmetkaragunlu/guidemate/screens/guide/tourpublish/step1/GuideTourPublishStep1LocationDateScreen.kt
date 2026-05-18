package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step1

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step1.content.GuideTourPublishStep1LocationDateContent

@Composable
fun GuideTourPublishStep1LocationDateScreen(
    uiState: GuideTourPublishUiState,
    onLocationClick: () -> Unit,
    onDateClick: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GuideTourPublishStep1LocationDateContent(
        uiState = uiState,
        onLocationClick = onLocationClick,
        onDateClick = onDateClick,
        onNext = onNext,
        modifier = modifier,
    )
}

