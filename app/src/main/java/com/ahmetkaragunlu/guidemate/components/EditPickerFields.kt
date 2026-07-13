package com.ahmetkaragunlu.guidemate.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ahmetkaragunlu.guidemate.R
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

private const val DURATION_STEP_MINUTES = 30
private const val MAX_DURATION_MINUTES = 12 * 60
private val defaultDurationOptions =
    (DURATION_STEP_MINUTES..MAX_DURATION_MINUTES step DURATION_STEP_MINUTES).toList()

@Composable
fun EditDatePickerField(
    @StringRes labelResId: Int,
    @StringRes placeholderResId: Int,
    selectedDate: LocalDate?,
    minimumDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit) = {
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = colorResource(R.color.brand_color),
        )
    },
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    var isPickerVisible by rememberSaveable { mutableStateOf(false) }

    PickerField(
        labelResId = labelResId,
        placeholderResId = placeholderResId,
        value = selectedDate?.toDisplayText().orEmpty(),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = { isPickerVisible = true },
        enabled = enabled,
        modifier = modifier,
    )

    if (isPickerVisible && enabled) {
        EditDatePickerDialog(
            selectedDate = selectedDate,
            minimumDate = minimumDate,
            onDateSelected = { date ->
                onDateSelected(date)
                isPickerVisible = false
            },
            onDismiss = { isPickerVisible = false },
        )
    }
}

@Composable
fun EditTimePickerField(
    @StringRes labelResId: Int,
    @StringRes placeholderResId: Int,
    selectedTime: LocalTime?,
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isTimeSelectable: (LocalTime) -> Boolean = { true },
    leadingIcon: @Composable (() -> Unit) = {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = null,
            tint = colorResource(R.color.brand_color),
        )
    },
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    var isPickerVisible by rememberSaveable { mutableStateOf(false) }

    PickerField(
        labelResId = labelResId,
        placeholderResId = placeholderResId,
        value = selectedTime?.toDisplayText().orEmpty(),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = { isPickerVisible = true },
        enabled = enabled,
        modifier = modifier,
    )

    if (isPickerVisible && enabled) {
        EditTimePickerDialog(
            selectedTime = selectedTime,
            initialTime = initialTime,
            isTimeSelectable = isTimeSelectable,
            onTimeSelected = { time ->
                onTimeSelected(time)
                isPickerVisible = false
            },
            onDismiss = { isPickerVisible = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDurationDropdown(
    @StringRes labelResId: Int,
    @StringRes placeholderResId: Int,
    selectedDurationMinutes: Int?,
    onDurationSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    durationOptions: List<Int> = defaultDurationOptions,
    leadingIcon: @Composable (() -> Unit) = {
        Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            tint = colorResource(R.color.brand_color),
        )
    },
) {
    var isExpanded by remember { mutableStateOf(false) }

    Text(
        text = stringResource(labelResId),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier,
    ) {
        EditTextField(
            value =
                selectedDurationMinutes?.let { duration ->
                    stringResource(R.string.tour_duration_format, duration)
                }.orEmpty(),
            onValueChange = { },
            readOnly = true,
            placeholder = placeholderResId,
            keyboardOptions = KeyboardOptions.Default,
            leadingIcon = leadingIcon,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = pickerFieldColors(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true,
                    ),
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            durationOptions.forEach { duration ->
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.tour_duration_format, duration))
                    },
                    onClick = {
                        onDurationSelected(duration)
                        isExpanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun PickerField(
    @StringRes labelResId: Int,
    @StringRes placeholderResId: Int,
    value: String,
    leadingIcon: @Composable (() -> Unit),
    trailingIcon: @Composable (() -> Unit)?,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(labelResId),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )
    Box(modifier = modifier) {
        EditTextField(
            value = value,
            onValueChange = { },
            enabled = enabled,
            readOnly = true,
            placeholder = placeholderResId,
            keyboardOptions = KeyboardOptions.Default,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            colors = pickerFieldColors(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
            modifier = Modifier.fillMaxWidth(),
        )
        Box(
            modifier =
                Modifier
                    .matchParentSize()
                    .clickable(
                        enabled = enabled,
                        onClick = onClick,
                    ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditDatePickerDialog(
    selectedDate: LocalDate?,
    minimumDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = selectedDate?.toUtcEpochMillis(),
            selectableDates =
                remember(minimumDate) {
                    object : SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                            utcTimeMillis >= minimumDate.toUtcEpochMillis()
                    }
                },
        )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = datePickerState.selectedDateMillis != null,
                onClick = {
                    datePickerState.selectedDateMillis?.let { selectedMillis ->
                        onDateSelected(selectedMillis.toLocalDate())
                    }
                },
            ) {
                Text(text = stringResource(R.string.select))
            }
        },
        dismissButton = { PickerCancelButton(onClick = onDismiss) },
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTimePickerDialog(
    selectedTime: LocalTime?,
    initialTime: LocalTime,
    isTimeSelectable: (LocalTime) -> Boolean,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
) {
    val pickerInitialTime = selectedTime ?: initialTime
    val timePickerState =
        rememberTimePickerState(
            initialHour = pickerInitialTime.hour,
            initialMinute = pickerInitialTime.minute,
            is24Hour = true,
        )
    val chosenTime = LocalTime.of(timePickerState.hour, timePickerState.minute)

    AlertDialog(
        onDismissRequest = onDismiss,
        text = { TimePicker(state = timePickerState) },
        confirmButton = {
            TextButton(
                enabled = isTimeSelectable(chosenTime),
                onClick = { onTimeSelected(chosenTime) },
            ) {
                Text(text = stringResource(R.string.select))
            }
        },
        dismissButton = { PickerCancelButton(onClick = onDismiss) },
    )
}

@Composable
private fun PickerCancelButton(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            text = stringResource(R.string.cancel),
            color = colorResource(R.color.text_color),
        )
    }
}

@Composable
private fun pickerFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFFEEEDF1),
        unfocusedBorderColor = Color(0xFFEEEDF1),
        cursorColor = Color.Transparent,
        unfocusedTextColor = colorResource(R.color.text_color),
    )

private fun LocalDate.toUtcEpochMillis(): Long =
    atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()

private fun LocalDate.toDisplayText(): String =
    format(
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault()),
    )

private fun LocalTime.toDisplayText(): String =
    format(
        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault()),
    )
