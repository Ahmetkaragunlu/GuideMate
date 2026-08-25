package com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.components

import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.model.ChangePasswordScreenState
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog

@Composable
fun HandleChangePasswordMessages(
    screenState: ChangePasswordScreenState,
    onErrorConsumed: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(screenState.errorMessage) {
        screenState.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            onErrorConsumed()
        }
    }
}

@Composable
fun ChangePasswordSuccessDialog(
    onConfirm: () -> Unit,
) {
    EditAlertDialog(
        title = R.string.password_changed_title,
        text = R.string.password_changed_message,
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.ok))
            }
        },
    )
}
