package com.ahmetkaragunlu.guidemate.auth.presentation.signin.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog

@Composable
internal fun VerificationRequiredDialog(
    isLoading: Boolean,
    cooldownSeconds: Long,
    onResend: () -> Unit,
    onDismiss: () -> Unit,
) {
    val canResend = !isLoading && cooldownSeconds == 0L
    val cooldownText =
        if (cooldownSeconds > 0) {
            val seconds = cooldownSeconds.toResourceQuantity()
            pluralStringResource(R.plurals.verification_resend_wait, seconds, seconds)
        } else {
            null
        }
    EditAlertDialog(
        title = R.string.verification_required_title,
        text = R.string.account_pending_verification_message,
        textValue = cooldownText,
        onDismissRequest = {
            if (!isLoading) onDismiss()
        },
        confirmButton = {
            TextButton(onClick = onResend, enabled = canResend) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = colorResource(R.color.brand_color),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(stringResource(R.string.resend_verification_email))
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading,
                colors =
                    ButtonDefaults.textButtonColors(
                        contentColor = colorResource(R.color.text_color),
                    ),
            ) {
                Text(stringResource(R.string.close))
            }
        },
    )
}
