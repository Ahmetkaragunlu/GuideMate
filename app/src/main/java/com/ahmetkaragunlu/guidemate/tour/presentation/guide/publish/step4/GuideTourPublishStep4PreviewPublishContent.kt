package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.step4

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.TourDetailContent
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.components.GuideTourPublishStepProgress
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.components.GuideTourPublishValidationMessage
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.mapper.toPreviewDetailUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishStep
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishUiState

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
            uiState.submissionErrorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        },
        isPrimaryActionLoading = uiState.isPublishing,
        isPrimaryActionEnabled = !uiState.publishSucceeded,
    )
}
