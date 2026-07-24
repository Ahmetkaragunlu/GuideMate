package com.ahmetkaragunlu.guidemate.screens.guide.tours.edit

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditDatePickerField
import com.ahmetkaragunlu.guidemate.components.EditDropdown
import com.ahmetkaragunlu.guidemate.components.EditTextField
import com.ahmetkaragunlu.guidemate.components.EditTimePickerField
import com.ahmetkaragunlu.guidemate.components.GuideMateImage
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategoryCatalog
import com.ahmetkaragunlu.guidemate.screens.guide.tours.components.GuideTourLanguageSelector
import com.ahmetkaragunlu.guidemate.screens.guide.tours.edit.model.GuideTourEditUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourApprovalStatus
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun GuideTourEditContent(
    uiState: GuideTourEditUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryClick: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onStartTimeSelected: (LocalTime) -> Unit,
    onMeetingPointChange: (String) -> Unit,
    onDurationChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCapacityChange: (String) -> Unit,
    onRemoveLanguage: (String) -> Unit,
    onAddLanguage: () -> Unit,
    onChangePhotos: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier =
                Modifier
                    .widthIn(max = 380.dp)
                    .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
            TourEditField(
                labelResId = R.string.guide_tour_publish_step3_name_label,
                value = uiState.title,
                onValueChange = onTitleChange,
                placeholderResId = R.string.guide_tour_publish_step3_name_placeholder,
            )
            TourEditMultilineField(
                labelResId = R.string.guide_tour_publish_step3_description_label,
                value = uiState.description,
                onValueChange = onDescriptionChange,
                placeholderResId = R.string.guide_tour_publish_step3_description_placeholder,
                height = 170.dp,
            )
            TourMediaEditor(
                uiState = uiState,
                onChangePhotos = onChangePhotos,
            )
            TourEditDropdownField(
                labelResId = R.string.guide_tour_publish_step1_location_label,
                value = listOf(uiState.country, uiState.location).joinToString(", "),
                placeholderResId = R.string.guide_tour_publish_step1_location_label,
                leadingText = "🌍",
            )
            TourEditDropdownField(
                labelResId = R.string.guide_tour_publish_step2_category_label,
                value =
                    uiState.category
                        ?.let(TourCategoryCatalog::uiModelFor)
                        ?.let { stringResource(it.titleResId) }
                        .orEmpty(),
                placeholderResId = R.string.guide_tour_publish_step2_category_label,
                leadingText = "🏷️",
                onClick = onCategoryClick,
            )
            if (uiState.isTourIdentityLocked) {
                Text(
                    text = stringResource(R.string.published_tour_identity_edit_lock),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_color),
                )
            }
            GuideTourLanguageSelector(
                languages = uiState.languages,
                onRemoveLanguage = onRemoveLanguage,
                onAddLanguage = onAddLanguage,
            )
            val today = LocalDate.now()
            EditDatePickerField(
                labelResId = R.string.guide_tour_publish_step1_date_label,
                placeholderResId = R.string.select_tour_date,
                selectedDate = uiState.tourDate,
                minimumDate = today,
                onDateSelected = onDateSelected,
                enabled = !uiState.hasBookings,
                leadingIcon = { Text(text = "🗓️") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = colorResource(R.color.text_color),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
            EditTimePickerField(
                labelResId = R.string.guide_tour_publish_step1_time_label,
                placeholderResId = R.string.select_tour_time,
                selectedTime = uiState.startTime,
                initialTime = LocalTime.now(),
                onTimeSelected = onStartTimeSelected,
                enabled = !uiState.hasBookings,
                isTimeSelectable = { selectedTime ->
                    uiState.tourDate != today || selectedTime.isAfter(LocalTime.now())
                },
                leadingIcon = { Text(text = "🕘") },
                modifier = Modifier.fillMaxWidth(),
            )
            TourEditField(
                labelResId = R.string.guide_tour_publish_step1_duration_label,
                value = uiState.durationMinutes,
                onValueChange = onDurationChange,
                enabled = !uiState.hasBookings,
                leadingText = "⏱️",
                keyboardType = KeyboardType.Number,
            )
            TourEditMultilineField(
                labelResId = R.string.guide_tour_publish_step3_meeting_label,
                value = uiState.meetingPoint,
                onValueChange = onMeetingPointChange,
                placeholderResId = R.string.guide_tour_publish_step3_meeting_placeholder,
                enabled = !uiState.hasBookings,
                height = 130.dp,
            )
            if (uiState.hasBookings) {
                Text(
                    text = stringResource(R.string.active_booking_edit_lock),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            TourEditNumericField(
                labelResId = R.string.guide_tour_publish_step2_price_label,
                value = uiState.price,
                onValueChange = onPriceChange,
                leadingText = "₺",
                useBrandTextColor = true,
            )
            TourEditNumericField(
                labelResId = R.string.guide_tour_publish_step2_capacity_label,
                value = uiState.capacity,
                onValueChange = onCapacityChange,
                leadingText = "👥",
                useBrandTextColor = false,
            )
            uiState.errorResId?.let { errorResId ->
                Text(
                    text = stringResource(errorResId),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
            EditButton(
                text =
                    if (uiState.approvalStatus == TourApprovalStatus.REJECTED) {
                        R.string.resubmit_for_review
                    } else {
                        R.string.save_changes
                    },
                onClick = onSave,
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_extra_large)))
        }
    }
}

@Composable
private fun TourMediaEditor(
    uiState: GuideTourEditUiState,
    onChangePhotos: () -> Unit,
) {
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
                .clip(RoundedCornerShape(dimensionResource(R.dimen.radius_medium)))
                .clickable(onClick = onChangePhotos),
        contentAlignment = Alignment.BottomCenter,
    ) {
        GuideMateImage(
            fallbackImageResId = uiState.coverImageResId,
            imageUrl = uiState.selectedCoverImageUri ?: uiState.coverImageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Text(
            text = stringResource(R.string.change_cover_photo),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_small)),
        )
    }
}

@Composable
private fun TourEditField(
    @StringRes labelResId: Int,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes placeholderResId: Int? = null,
    leadingText: String? = null,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Text(
        text = stringResource(labelResId),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )
    EditTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        placeholder = placeholderResId,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = leadingText?.let { { Text(text = it) } },
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFEEEDF1),
                unfocusedBorderColor = Color(0xFFEEEDF1),
                unfocusedTextColor = colorResource(R.color.text_color),
            ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun TourEditDropdownField(
    @StringRes labelResId: Int,
    value: String,
    @StringRes placeholderResId: Int,
    leadingText: String,
    onClick: () -> Unit = {},
) {
    Text(
        text = stringResource(labelResId),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )
    EditDropdown(
        value = value,
        placeholder = placeholderResId,
        onClick = onClick,
        leadingIcon = { Text(text = leadingText) },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun TourEditNumericField(
    @StringRes labelResId: Int,
    value: String,
    onValueChange: (String) -> Unit,
    leadingText: String,
    useBrandTextColor: Boolean,
) {
    Text(
        text = stringResource(labelResId),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
        color = colorResource(R.color.brand_color),
    )
    EditTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        leadingIcon = {
            if (useBrandTextColor) {
                Text(
                    text = leadingText,
                    color = colorResource(R.color.brand_color),
                )
            } else {
                Text(text = leadingText)
            }
        },
        colors =
            if (useBrandTextColor) {
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFEEEDF1),
                    unfocusedBorderColor = Color(0xFFEEEDF1),
                    cursorColor = Color.Transparent,
                    focusedTextColor = colorResource(R.color.brand_color),
                    unfocusedTextColor = colorResource(R.color.brand_color),
                )
            } else {
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFEEEDF1),
                    unfocusedBorderColor = Color(0xFFEEEDF1),
                    cursorColor = Color.Transparent,
                )
            },
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun TourEditMultilineField(
    @StringRes labelResId: Int,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes placeholderResId: Int,
    enabled: Boolean = true,
    height: Dp,
) {
    Text(
        text = stringResource(labelResId),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        placeholder = {
            Text(
                text = stringResource(placeholderResId),
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
                .height(height),
    )
}
