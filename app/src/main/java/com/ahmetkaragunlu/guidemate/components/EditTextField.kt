package com.ahmetkaragunlu.guidemate.components

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R


@Composable
fun EditTextField(
    value: String,
    isError: Boolean = false,
    onValueChange: (String) -> Unit,
    @StringRes placeholder : Int? = null,
    @StringRes supportingText: Int? = null,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.LightGray,
        unfocusedBorderColor = Color.LightGray
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        shape = shape,
        placeholder = placeholder?.let {  { Text(text = stringResource(it), style = MaterialTheme.typography.labelLarge, color = Color.Gray) } },
        supportingText = supportingText?.let { { Text(text = stringResource(it), color = MaterialTheme.colorScheme.error) } },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        colors = colors,
        isError = isError,
        singleLine = true,
    )


}