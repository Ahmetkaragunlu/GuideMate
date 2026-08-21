package com.ahmetkaragunlu.guidemate.common.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import com.ahmetkaragunlu.guidemate.R

@Composable
fun EditTextField(
    value: String,
    isError: Boolean = false,
    onValueChange: (String) -> Unit,
    @StringRes placeholder: Int? = null,
    placeholderText: String? = null,
    @StringRes supportingText: Int? = null,
    supportingTextValue: String? = null,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholderColor: Color = Color.Gray,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
    colors: TextFieldColors =
        OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.LightGray,
        ),
) {
    OutlinedTextField(
        value = value,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        onValueChange = onValueChange,
        shape = shape,
        placeholder = {
            when {
                placeholder != null -> {
                    Text(
                        text = stringResource(placeholder),
                        style = MaterialTheme.typography.labelLarge,
                        color = placeholderColor,
                    )
                }

                !placeholderText.isNullOrBlank() -> {
                    Text(
                        text = placeholderText,
                        style = MaterialTheme.typography.labelLarge,
                        color = placeholderColor,
                    )
                }
            }
        },
        supportingText =
            when {
                supportingText != null -> {
                    {
                        Text(
                            text = stringResource(supportingText),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }

                !supportingTextValue.isNullOrBlank() -> {
                    {
                        Text(
                            text = supportingTextValue,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }

                else -> null
            },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        colors = colors,
        isError = isError,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
    )
}
