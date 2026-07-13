package com.ahmetkaragunlu.guidemate.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.ahmetkaragunlu.guidemate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDropdown(
    value: String,
    @StringRes placeholder: Int,
    modifier: Modifier = Modifier,
    options: List<String> = emptyList(),
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val expanded = false

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { },
        modifier = modifier,
    ) {
        EditTextField(
            value = value,
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFeeedf1),
                    unfocusedBorderColor = Color(0xFFeeedf1),
                    cursorColor = Color.Transparent,
                    unfocusedTextColor = colorResource(R.color.text_color),
                ),
            onValueChange = {},
            readOnly = true,
            placeholder = placeholder,
            keyboardOptions = KeyboardOptions.Default,
            leadingIcon = leadingIcon,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true,
                    ),
        )
    }
}
