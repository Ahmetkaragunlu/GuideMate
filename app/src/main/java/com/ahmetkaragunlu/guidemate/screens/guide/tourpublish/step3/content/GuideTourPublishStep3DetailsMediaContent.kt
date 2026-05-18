package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step3.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditTextField
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.components.GuideTourPublishStepProgress
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import compose.icons.TablerIcons
import compose.icons.tablericons.CameraPlus

@Composable
fun GuideTourPublishStep3DetailsMediaContent(
    uiState: GuideTourPublishUiState,
    onTourNameChange: (String) -> Unit,
    onUploadPhotosClick: () -> Unit,
    onDescriptionChange: (String) -> Unit,
    onMeetingPointChange: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
    ) {
        Column(
            modifier =
                Modifier
                    .widthIn(max = 380.dp)
                    .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
        ) {
            GuideTourPublishStepProgress(
                progressLabelResId = R.string.guide_tour_publish_step3_progress_label,
                filledStepIndexInclusive = 2,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_medium)),
            )
            Step3Header()
            Step3TourNameField(
                value = uiState.tourName,
                onTourNameChange = onTourNameChange,
            )
            Step3UploadArea(onClick = onUploadPhotosClick)
            Step3DescriptionField(
                description = uiState.tourDescription,
                onDescriptionChange = onDescriptionChange,
            )
            Step3MeetingPointField(
                meetingPoint = uiState.meetingPoint,
                onMeetingPointChange = onMeetingPointChange,
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
        EditButton(
            text = R.string.guide_tour_publish_step3_next_button,
            onClick = onNext,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_large)),
        )
    }
}

@Composable
private fun Step3Header() {
    Text(
        text = stringResource(R.string.guide_tour_publish_step3_title),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )

    Text(
        text = stringResource(R.string.guide_tour_publish_step3_subtitle),
        style = MaterialTheme.typography.bodyMedium,
        color = colorResource(R.color.text_color),
    )
}

@Composable
private fun Step3TourNameField(
    value: String,
    onTourNameChange: (String) -> Unit,
) {
    Text(
        text = stringResource(R.string.guide_tour_publish_step3_name_label),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )

    EditTextField(
        value = value,
        onValueChange = onTourNameChange,
        placeholder = R.string.guide_tour_publish_step3_name_placeholder,
        keyboardOptions = KeyboardOptions.Default,
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFEEEDF1),
                unfocusedBorderColor = Color(0xFFEEEDF1),
            ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun Step3UploadArea(
    onClick: () -> Unit,
) {
    val uploadCornerRadius = dimensionResource(R.dimen.radius_medium)

    Text(
        text = stringResource(R.string.guide_tour_publish_step3_photos_label),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(
                    color = colorResource(R.color.brand_color).copy(alpha = 0.04f),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                )
                .drawBehind {
                    drawRoundRect(
                        color = Color(0xFFE0E0E0),
                        style =
                            Stroke(
                                width = 3f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f),
                            ),
                        cornerRadius = CornerRadius(uploadCornerRadius.toPx()),
                    )
                }
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(56.dp)
                        .background(
                            color = colorResource(R.color.brand_color).copy(alpha = 0.14f),
                            shape = RoundedCornerShape(16.dp),
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = TablerIcons.CameraPlus,
                    contentDescription = null,
                    tint = colorResource(R.color.brand_color),
                    modifier = Modifier.size(28.dp),
                )
            }
            Text(
                text = stringResource(R.string.guide_tour_publish_step3_photos_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(R.color.brand_color),
            )
            Text(
                text = stringResource(R.string.guide_tour_publish_step3_photos_placeholder),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_color),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
            )
            Text(
                text = stringResource(R.string.guide_tour_publish_step3_photos_meta),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
            )
        }
    }
}

@Composable
private fun Step3DescriptionField(
    description: String,
    onDescriptionChange: (String) -> Unit,
) {
    Text(
        text = stringResource(R.string.guide_tour_publish_step3_description_label),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )

    OutlinedTextField(
        value = description,
        onValueChange = onDescriptionChange,
        placeholder = {
            Text(
                text = stringResource(R.string.guide_tour_publish_step3_description_placeholder),
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray,
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFEEEDF1),
                unfocusedBorderColor = Color(0xFFEEEDF1),
            ),
        modifier =
            Modifier
                .fillMaxWidth()
                .height(170.dp),
    )
}

@Composable
private fun Step3MeetingPointField(
    meetingPoint: String,
    onMeetingPointChange: (String) -> Unit,
) {
    Text(
        text = stringResource(R.string.guide_tour_publish_step3_meeting_label),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )

    OutlinedTextField(
        value = meetingPoint,
        onValueChange = onMeetingPointChange,
        placeholder = {
            Text(
                text = stringResource(R.string.guide_tour_publish_step3_meeting_placeholder),
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray,
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFEEEDF1),
                unfocusedBorderColor = Color(0xFFEEEDF1),
            ),
        modifier =
            Modifier
                .fillMaxWidth()
                .height(130.dp),
    )
}
