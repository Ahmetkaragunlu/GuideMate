package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step3.content.GuideTourPublishStep3DetailsMediaContent
import com.ahmetkaragunlu.guidemate.screens.guide.tours.components.TourCoverImagePicker

@Composable
fun GuideTourPublishStep3DetailsMediaScreen(
    uiState: GuideTourPublishUiState,
    onTourNameChange: (String) -> Unit,
    onCoverImageSelected: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onMeetingPointChange: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPhotoSourceSheet by rememberSaveable { mutableStateOf(false) }
    var coverImageErrorResId by rememberSaveable { mutableStateOf<Int?>(null) }

    GuideTourPublishStep3DetailsMediaContent(
        uiState = uiState,
        onTourNameChange = onTourNameChange,
        onUploadPhotosClick = { showPhotoSourceSheet = true },
        coverImageErrorResId = coverImageErrorResId,
        onDescriptionChange = onDescriptionChange,
        onMeetingPointChange = onMeetingPointChange,
        onNext = onNext,
        modifier = modifier,
    )

    TourCoverImagePicker(
        isVisible = showPhotoSourceSheet,
        onDismissRequest = { showPhotoSourceSheet = false },
        onImageSelected = { uri ->
            coverImageErrorResId = null
            onCoverImageSelected(uri)
        },
        onError = { errorResId -> coverImageErrorResId = errorResId },
    )
}
