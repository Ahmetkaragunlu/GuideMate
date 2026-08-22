package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditTextField

@Composable
fun CancelTourSessionDialog(
    reason: String,
    hasBookings: Boolean,
    onReasonChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isSubmitting: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = { if (!isSubmitting) onDismiss() },
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
                enabled = reason.isNotBlank() && !isSubmitting,
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = MaterialTheme.colorScheme.error,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = stringResource(R.string.confirm_cancellation),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSubmitting) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    )
}
