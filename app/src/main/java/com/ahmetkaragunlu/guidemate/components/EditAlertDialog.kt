package com.ahmetkaragunlu.guidemate.components

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight

@Composable
fun EditAlertDialog(
    @StringRes title: Int,
    @StringRes text: Int,
    textValue: String? = null,
    textFormatArguments: List<Any> = emptyList(),
    textModifier: Modifier = Modifier,
    compactText: Boolean = false,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    onDismissRequest: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(id = title),
                style =
                    if (compactText) {
                        MaterialTheme.typography.titleMedium
                    } else {
                        LocalTextStyle.current
                    },
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(
                modifier = textModifier,
                text =
                    textValue
                        ?: stringResource(
                            id = text,
                            formatArgs = textFormatArguments.toTypedArray(),
                        ),
                style =
                    if (compactText) {
                        MaterialTheme.typography.bodySmall
                    } else {
                        LocalTextStyle.current
                    },
            )
        },
        confirmButton = confirmButton,
        dismissButton = dismissButton,
    )
}
