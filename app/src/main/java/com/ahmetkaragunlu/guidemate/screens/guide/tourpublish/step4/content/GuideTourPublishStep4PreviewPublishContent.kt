package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.content

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.TourDetailContent
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.components.GuideTourPublishStepProgress
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.components.GuideTourPublishValidationMessage
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.mapper.toPreviewDetailUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishStep
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState

@Composable
fun GuideTourPublishStep4PreviewPublishContent(
    uiState: GuideTourPublishUiState,
    onPublish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TourDetailContent(
        uiState = uiState.toPreviewDetailUiState(),
        mode = TourDetailMode.PREVIEW,
        onPrimaryAction = onPublish,
        modifier = modifier,
        topContent = {
            GuideTourPublishStepProgress(
                progressLabelResId = R.string.guide_tour_publish_step4_progress_label,
                filledStepIndexInclusive = 3,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_medium)),
            )
            GuideTourPublishValidationMessage(
                errorResId = uiState.validationErrorFor(GuideTourPublishStep.PREVIEW),
            )
        },
    )
}
