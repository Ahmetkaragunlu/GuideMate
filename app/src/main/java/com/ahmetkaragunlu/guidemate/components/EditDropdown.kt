package com.ahmetkaragunlu.guidemate.components


import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDropdown(
    value: String,
    @StringRes placeholder: Int,
    modifier: Modifier = Modifier,
    options: List<String> = emptyList()
) {

    val expanded = false

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { },
        modifier = modifier
    ) {
        EditTextField(
            value = value,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFeeedf1),
                unfocusedBorderColor = Color(0xFFeeedf1),
                ),
            onValueChange = {},
            readOnly = true,
            placeholder = placeholder,
            keyboardOptions = KeyboardOptions.Default,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

    }
}