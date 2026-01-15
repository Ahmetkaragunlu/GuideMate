package com.ahmetkaragunlu.guidemate.components


import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight


@Composable
fun EditAlertDialog(
    @StringRes title: Int,
    @StringRes text: Int,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    onDismissRequest: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(id = title),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(text = stringResource(id = text))
        },
        confirmButton = confirmButton,
        dismissButton = dismissButton,
    )
}