package com.ahmetkaragunlu.guidemate.common.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun EditAlertDialog(
    @StringRes title: Int,
    @StringRes text: Int,
    confirmButton: @Composable () -> Unit,
    textVerticalOffset: Dp = 0.dp,
    textValue: String? = null,
    textFormatArguments: List<Any> = emptyList(),
    compactText: Boolean = false,
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
                modifier = Modifier.offset(y = textVerticalOffset),
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
