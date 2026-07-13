package com.ahmetkaragunlu.guidemate.screens.guide.tours.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditDatePickerField
import com.ahmetkaragunlu.guidemate.components.EditDurationDropdown
import com.ahmetkaragunlu.guidemate.components.EditTimePickerField
import com.ahmetkaragunlu.guidemate.screens.guide.tours.components.GuideTourCapacityField
import com.ahmetkaragunlu.guidemate.screens.guide.tours.components.GuideTourMeetingPointField
import com.ahmetkaragunlu.guidemate.screens.guide.tours.components.GuideTourPriceField
import com.ahmetkaragunlu.guidemate.screens.guide.tours.detail.model.NewTourSessionFormState
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTourSessionBottomSheet(
    formState: NewTourSessionFormState,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (LocalTime) -> Unit,
    onDurationSelected: (Int) -> Unit,
    onMeetingPointChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCapacityChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val tourZoneId =
        remember(formState.timeZoneId) {
            runCatching { ZoneId.of(formState.timeZoneId) }.getOrDefault(ZoneId.systemDefault())
        }
    val today = LocalDate.now(tourZoneId)

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier =
                    Modifier
                        .widthIn(max = 380.dp)
                        .fillMaxWidth(),
                verticalArrangement =
                    Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
            ) {
                Text(
                    text = stringResource(R.string.republish_tour_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                EditDatePickerField(
                    labelResId = R.string.guide_tour_publish_step1_date_label,
                    placeholderResId = R.string.select_tour_date,
                    selectedDate = formState.selectedDate,
                    minimumDate = today,
                    onDateSelected = onDateSelected,
                    modifier = Modifier.fillMaxWidth(),
                )
                EditTimePickerField(
                    labelResId = R.string.guide_tour_publish_step1_time_label,
                    placeholderResId = R.string.select_tour_time,
                    selectedTime = formState.selectedTime,
                    initialTime = LocalTime.now(tourZoneId),
                    isTimeSelectable = { selectedTime ->
                        formState.selectedDate != today ||
                            selectedTime.isAfter(LocalTime.now(tourZoneId))
                    },
                    onTimeSelected = onTimeSelected,
                    modifier = Modifier.fillMaxWidth(),
                )
                EditDurationDropdown(
                    labelResId = R.string.guide_tour_publish_step1_duration_label,
                    placeholderResId = R.string.select_tour_duration,
                    selectedDurationMinutes = formState.durationMinutes,
                    onDurationSelected = onDurationSelected,
                    modifier = Modifier.fillMaxWidth(),
                )
                GuideTourMeetingPointField(
                    value = formState.meetingPoint,
                    onValueChange = onMeetingPointChange,
                )
                GuideTourPriceField(
                    value = formState.price,
                    onValueChange = onPriceChange,
                )
                GuideTourCapacityField(
                    value = formState.capacity,
                    onValueChange = onCapacityChange,
                )
                formState.errorResId?.let { messageResId ->
                    Text(
                        text = stringResource(messageResId),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
                EditButton(
                    text = R.string.republish_tour,
                    onClick = onConfirm,
                    enabled = formState.canSubmit,
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
            }
        }
    }
}
