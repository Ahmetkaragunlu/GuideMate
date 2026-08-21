package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton
import com.ahmetkaragunlu.guidemate.common.ui.components.EditDatePickerField
import com.ahmetkaragunlu.guidemate.common.ui.components.EditTimePickerField
import com.ahmetkaragunlu.guidemate.common.ui.formatting.platformCurrencySymbol
import com.ahmetkaragunlu.guidemate.tour.presentation.category.TourCategoryCatalog
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.components.GuideTourLanguageSelector
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditUiState
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
                leadingText = platformCurrencySymbol(),
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
