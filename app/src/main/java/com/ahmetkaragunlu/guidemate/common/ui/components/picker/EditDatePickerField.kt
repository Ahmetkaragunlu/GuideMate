package com.ahmetkaragunlu.guidemate.common.ui.components.picker

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.ahmetkaragunlu.guidemate.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

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

private fun LocalDate.toUtcEpochMillis(): Long =
    atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()

private fun LocalDate.toDisplayText(): String =
    format(
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault()),
    )
