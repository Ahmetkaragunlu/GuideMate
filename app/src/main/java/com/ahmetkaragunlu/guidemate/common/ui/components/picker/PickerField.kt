package com.ahmetkaragunlu.guidemate.common.ui.components.picker

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditTextField

@Composable
internal fun PickerField(
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
        style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
    )
    Box(modifier = modifier) {
        EditTextField(
            value = value,
            onValueChange = {},
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
                    .clickable(enabled = enabled, onClick = onClick),
        )
    }
}

@Composable
internal fun PickerCancelButton(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            text = stringResource(R.string.cancel),
            color = colorResource(R.color.text_color),
        )
    }
}

@Composable
internal fun pickerFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = colorResource(R.color.border_color),
        unfocusedBorderColor = colorResource(R.color.border_color),
        cursorColor = Color.Transparent,
        unfocusedTextColor = colorResource(R.color.text_color),
    )
