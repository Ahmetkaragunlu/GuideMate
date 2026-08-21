package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.step4

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishUiState

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
