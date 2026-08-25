package com.ahmetkaragunlu.guidemate.auth.presentation.signin

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.auth.presentation.signin.components.SignInScreenContent
import com.ahmetkaragunlu.guidemate.auth.presentation.signin.components.VerificationRequiredDialog
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
                    is GoogleSignInResult.Success -> viewModel.onGoogleSignInSuccess(result.idToken)
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
