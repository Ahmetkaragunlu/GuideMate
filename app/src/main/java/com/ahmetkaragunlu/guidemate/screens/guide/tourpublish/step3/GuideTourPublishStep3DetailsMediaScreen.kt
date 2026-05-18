package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step3

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step3.content.GuideTourPublishStep3DetailsMediaContent

@Composable
fun GuideTourPublishStep3DetailsMediaScreen(
    uiState: GuideTourPublishUiState,
    onTourNameChange: (String) -> Unit,
    onUploadPhotosClick: () -> Unit,
    onDescriptionChange: (String) -> Unit,
    onMeetingPointChange: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GuideTourPublishStep3DetailsMediaContent(
        uiState = uiState,
        onTourNameChange = onTourNameChange,
        onUploadPhotosClick = onUploadPhotosClick,
        onDescriptionChange = onDescriptionChange,
        onMeetingPointChange = onMeetingPointChange,
        onNext = onNext,
        modifier = modifier,
    )
}

