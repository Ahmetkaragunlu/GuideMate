package com.ahmetkaragunlu.guidemate.auth.presentation.signup.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog

@Composable
internal fun RegistrationSuccessDialog(onDismiss: () -> Unit) {
    EditAlertDialog(
        title = R.string.verification_required_title,
        text = R.string.registration_success_message,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.brand_color)),
            ) {
                Text(stringResource(R.string.ok))
            }
        },
    )
}
