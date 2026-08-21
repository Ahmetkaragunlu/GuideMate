package com.ahmetkaragunlu.guidemate.auth.presentation.forgotpassword

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton
import com.ahmetkaragunlu.guidemate.common.ui.components.EditTextField

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onNavigateToSignIn: () -> Unit,
) {
    val context = LocalContext.current
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(screenState.errorMessage) {
        screenState.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    ForgotPasswordScreenContent(
        modifier = modifier,
        formState = formState,
        screenState = screenState,
        isEmailValid = viewModel.isValidEmail(),
        onEmailChange = viewModel::onEmailChange,
        onSubmitClick = viewModel::onSubmitClick,
        onDismissSuccessDialog = {
            viewModel.dismissSuccessDialog()
            onNavigateToSignIn()
        },
    )
}

@Composable
private fun ForgotPasswordScreenContent(
    modifier: Modifier = Modifier,
    formState: ForgotPasswordFormState,
    screenState: ForgotPasswordScreenState,
    isEmailValid: Boolean,
    onEmailChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onDismissSuccessDialog: () -> Unit,
) {
    if (screenState.showSuccessDialog) {
        ForgotPasswordSuccessDialog(onDismiss = onDismissSuccessDialog)
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    top = dimensionResource(R.dimen.spacing_double_extra_large),
                    start = dimensionResource(R.dimen.spacing_medium),
                    end = dimensionResource(R.dimen.spacing_medium),
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.reset_password_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
        Text(
            text = stringResource(R.string.reset_password_description),
            color = colorResource(R.color.text_color),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        EditTextField(
            value = formState.email,
            onValueChange = onEmailChange,
            placeholder = R.string.email,
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done,
                ),
            isError =
                (!isEmailValid && formState.email.isNotEmpty()) ||
                    screenState.emailErrorMessage != null,
            supportingText =
                if (!isEmailValid && formState.email.isNotEmpty()) {
                    R.string.email_error_message
                } else {
                    null
                },
            supportingTextValue = screenState.emailErrorMessage,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
        EditButton(
            text = R.string.send_reset_link,
            onClick = onSubmitClick,
            enabled = screenState.retryAfterSeconds == 0L,
            isLoading = screenState.isLoading,
        )
        if (screenState.retryAfterSeconds > 0) {
            val seconds =
                screenState.retryAfterSeconds.coerceIn(0, Int.MAX_VALUE.toLong()).toInt()
            Text(
                text =
                    pluralStringResource(
                        R.plurals.password_reset_retry_wait,
                        seconds,
                        seconds,
                    ),
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_small)),
                color = colorResource(R.color.text_color),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun ForgotPasswordSuccessDialog(
    onDismiss: () -> Unit,
) {
    EditAlertDialog(
        title = R.string.success,
        text = R.string.reset_password_link_sent,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.ok))
            }
        },
    )
}
