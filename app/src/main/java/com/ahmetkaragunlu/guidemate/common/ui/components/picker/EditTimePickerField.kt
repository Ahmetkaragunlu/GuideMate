package com.ahmetkaragunlu.guidemate.common.ui.components.picker

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.ahmetkaragunlu.guidemate.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

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

private fun LocalTime.toDisplayText(): String =
    format(
        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault()),
    )
