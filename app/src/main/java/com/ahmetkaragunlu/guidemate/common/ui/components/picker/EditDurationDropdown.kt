package com.ahmetkaragunlu.guidemate.common.ui.components.picker

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditTextField

private const val DURATION_STEP_MINUTES = 30
private const val MAX_DURATION_MINUTES = 12 * 60
private val defaultDurationOptions =
    (DURATION_STEP_MINUTES..MAX_DURATION_MINUTES step DURATION_STEP_MINUTES).toList()

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
            onValueChange = {},
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
