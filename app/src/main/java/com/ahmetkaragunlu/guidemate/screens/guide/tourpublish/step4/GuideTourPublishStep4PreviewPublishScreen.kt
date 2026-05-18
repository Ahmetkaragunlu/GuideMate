package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.content.GuideTourPublishStep4PreviewPublishContent

@Composable
fun GuideTourPublishStep4PreviewPublishScreen(
    uiState: GuideTourPublishUiState,
    onPublish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GuideTourPublishStep4PreviewPublishContent(
        uiState = uiState,
        onPublish = onPublish,
        modifier = modifier,
    )
}

