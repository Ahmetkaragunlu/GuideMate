package com.ahmetkaragunlu.guidemate.screens.guide.tours.detail

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditTextField

@Composable
fun CancelTourSessionDialog(
    reason: String,
    hasBookings: Boolean,
    onReasonChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.cancel_tour_title)) },
        text = {
            Column {
                Text(
                    text =
                        stringResource(
                            if (hasBookings) {
                                R.string.cancel_tour_warning
                            } else {
                                R.string.cancel_tour_no_booking_warning
                            },
                        ),
                    style = MaterialTheme.typography.bodyMedium,
                )
                EditTextField(
                    value = reason,
                    onValueChange = onReasonChange,
                    placeholder = R.string.cancellation_reason,
                    keyboardOptions = KeyboardOptions.Default,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = reason.isNotBlank(),
            ) {
                Text(
                    text = stringResource(R.string.confirm_cancellation),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    )
}
