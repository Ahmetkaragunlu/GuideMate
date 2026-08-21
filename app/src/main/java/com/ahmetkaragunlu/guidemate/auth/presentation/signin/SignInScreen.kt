package com.ahmetkaragunlu.guidemate.auth.presentation.signin

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton
import com.ahmetkaragunlu.guidemate.common.ui.components.EditTextField
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
) {
    val context = LocalContext.current
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val googleSignInClient = remember(context) { GoogleCredentialSignInClient.create(context) }

    LaunchedEffect(screenState.errorMessage) {
        screenState.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    LaunchedEffect(screenState.infoMessage) {
        screenState.infoMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.clearInfoMessage()
        }
    }

    if (screenState.showVerificationDialog) {
        VerificationRequiredDialog(
            isLoading = screenState.isResendingVerification,
            cooldownSeconds = screenState.resendCooldownSeconds,
            onResend = viewModel::resendVerificationEmail,
            onDismiss = viewModel::dismissVerificationDialog,
        )
    }

    SignInScreenContent(
        modifier = modifier,
        formState = formState,
        screenState = screenState,
        isEmailValid = viewModel.isValidEmail(),
        isPasswordValid = viewModel.isValidPassword(),
        onGoogleSignInClick = {
            viewModel.onGoogleSignInStarted()
            coroutineScope.launch {
                when (val result = googleSignInClient.signIn(context, viewModel.webClientId)) {
                    is GoogleSignInResult.Success ->
                        viewModel.onGoogleSignInSuccess(result.idToken)
                    GoogleSignInResult.Cancelled -> viewModel.onGoogleSignInCancelled()
                    GoogleSignInResult.Failure -> viewModel.onGoogleSignInFailure()
                }
            }
        },
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
        onNavigateToForgotPassword = onNavigateToForgotPassword,
        onSignInClick = viewModel::onSignInClick,
        onNavigateToSignUp = onNavigateToSignUp,
    )
}

@Composable
private fun SignInScreenContent(
    modifier: Modifier = Modifier,
    formState: SignInFormState,
    screenState: SignInScreenState,
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    onGoogleSignInClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onSignInClick: () -> Unit,
    onNavigateToSignUp: () -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    top = dimensionResource(R.dimen.spacing_double_extra_large),
                    bottom = dimensionResource(R.dimen.spacing_large),
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.sign_in_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
        Text(
            text = stringResource(R.string.sign_in_subtitle),
            color = colorResource(R.color.text_color),
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_double_extra_large)))

        SignInFormSection(
            formState = formState,
            isEmailValid = isEmailValid,
            isPasswordValid = isPasswordValid,
            emailErrorMessage = screenState.emailErrorMessage,
            passwordErrorMessage = screenState.passwordErrorMessage,
            isLoading = screenState.isLoading,
            retryAfterSeconds = screenState.loginRetryAfterSeconds,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            onNavigateToForgotPassword = onNavigateToForgotPassword,
            onSignInClick = onSignInClick,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

        OrContinueDivider()

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_extra_large)))

        GoogleSignInButton(
            enabled = !screenState.isLoading,
            onClick = onGoogleSignInClick,
        )

        Spacer(modifier = Modifier.weight(1f))

        SignUpTextButton(onNavigateToSignUp)
    }
}

@Composable
private fun SignInFormSection(
    formState: SignInFormState,
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    emailErrorMessage: String?,
    passwordErrorMessage: String?,
    isLoading: Boolean,
    retryAfterSeconds: Long,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onSignInClick: () -> Unit,
) {
    EditTextField(
        value = formState.email,
        onValueChange = onEmailChange,
        placeholder = R.string.email,
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = Color.Gray,
            )
        },
        isError = (!isEmailValid && formState.email.isNotEmpty()) || emailErrorMessage != null,
        supportingText =
            if (!isEmailValid && formState.email.isNotEmpty()) {
                R.string.email_error_message
            } else {
                null
            },
        supportingTextValue = emailErrorMessage,
    )

    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

    EditTextField(
        value = formState.password,
        onValueChange = onPasswordChange,
        placeholder = R.string.password,
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done,
            ),
        visualTransformation =
            if (formState.passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color.Gray,
            )
        },
        trailingIcon = {
            Icon(
                imageVector =
                    if (formState.passwordVisibility) {
                        Icons.Default.Visibility
                    } else {
                        Icons.Default.VisibilityOff
                    },
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.clickable { onTogglePasswordVisibility() },
            )
        },
        isError = (!isPasswordValid && formState.password.isNotEmpty()) || passwordErrorMessage != null,
        supportingText =
            if (!isPasswordValid && formState.password.isNotEmpty()) {
                R.string.password_error_message
            } else {
                null
            },
        supportingTextValue = passwordErrorMessage,
    )

    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))

    Row(
        modifier =
            Modifier
                .widthIn(max = 380.dp)
                .fillMaxWidth()
                .padding(horizontal = 56.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        Text(
            text = stringResource(R.string.forgot_password),
            modifier = Modifier.clickable { onNavigateToForgotPassword() },
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.text_color),
        )
    }

    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

    EditButton(
        text = R.string.login,
        onClick = onSignInClick,
        enabled = retryAfterSeconds == 0L,
        isLoading = isLoading,
    )
    if (retryAfterSeconds > 0) {
        val seconds = retryAfterSeconds.toResourceQuantity()
        Text(
            text =
                pluralStringResource(
                    R.plurals.login_retry_wait,
                    seconds,
                    seconds,
                ),
            modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_small)),
            color = colorResource(R.color.text_color),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun OrContinueDivider() {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Color.Gray.copy(alpha = 0.4f),
            thickness = 1.dp,
        )
        Text(
            text = stringResource(R.string.or_continue_with),
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_small)),
            style = MaterialTheme.typography.bodySmall,
            color = colorResource(R.color.text_color),
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Color.Gray.copy(alpha = 0.4f),
            thickness = 1.dp,
        )
    }
}

@Composable
private fun GoogleSignInButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        modifier =
            Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_extra_large)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
    ) {
        Icon(
            painter = painterResource(R.drawable.google_icon),
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
        Text(
            text = stringResource(R.string.google),
            style = MaterialTheme.typography.labelLarge,
            color = colorResource(R.color.text_color).copy(alpha = 0.8f),
        )
    }
}

@Composable
private fun VerificationRequiredDialog(
    isLoading: Boolean,
    cooldownSeconds: Long,
    onResend: () -> Unit,
    onDismiss: () -> Unit,
) {
    val canResend = !isLoading && cooldownSeconds == 0L
    val cooldownText =
        if (cooldownSeconds > 0) {
            val seconds = cooldownSeconds.toResourceQuantity()
            pluralStringResource(
                R.plurals.verification_resend_wait,
                seconds,
                seconds,
            )
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
            TextButton(
                onClick = onResend,
                enabled = canResend,
            ) {
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
            ) {
                Text(stringResource(R.string.close))
            }
        },
    )
}

private fun Long.toResourceQuantity(): Int =
    coerceIn(0, Int.MAX_VALUE.toLong()).toInt()

@Composable
private fun SignUpTextButton(onNavigateToSignUp: () -> Unit) {
    TextButton(
        onClick = { onNavigateToSignUp() },
    ) {
        Text(
            text =
                buildAnnotatedString {
                    withStyle(
                        style =
                            SpanStyle(
                                color = colorResource(R.color.text_color),
                            ),
                    ) {
                        append(stringResource(R.string.dont_have_an_account))
                        append("  ")
                    }
                    withStyle(
                        style =
                            SpanStyle(
                                color = colorResource(R.color.brand_color),
                            ),
                    ) {
                        append(stringResource(R.string.sign_up_text))
                    }
                },
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
