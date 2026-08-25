package com.ahmetkaragunlu.guidemate.auth.presentation.signup

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.auth.presentation.signup.components.RegistrationSuccessDialog
import com.ahmetkaragunlu.guidemate.auth.presentation.signup.components.SignUpScreenContent
import com.ahmetkaragunlu.guidemate.auth.presentation.signup.components.TermsBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateToSignIn: () -> Unit,
) {
    val context = LocalContext.current
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(screenState.errorMessage) {
        screenState.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    if (screenState.isRegistrationSuccess) {
        RegistrationSuccessDialog(
            onDismiss = {
                viewModel.resetRegistrationState()
                onNavigateToSignIn()
            },
        )
    }

    if (screenState.showTermsSheet) {
        TermsBottomSheet(
            sheetState = sheetState,
            hasUserReadTerms = screenState.hasUserReadTerms,
            onDismiss = { viewModel.toggleTermsSheet(false) },
            onMarkTermsAsRead = viewModel::markTermsAsRead,
            onAcceptTerms = viewModel::acceptTerms,
        )
    }

    SignUpScreenContent(
        modifier = modifier,
        formState = formState,
        screenState = screenState,
        isFirstNameValid = viewModel.isValidFirstName(),
        isLastNameValid = viewModel.isValidLastName(),
        isEmailValid = viewModel.isValidEmail(),
        isPasswordValid = viewModel.isValidPassword(),
        isConfirmPasswordValid = viewModel.isValidConfirmPassword(),
        onFirstNameChange = viewModel::inputFirstNameChange,
        onLastNameChange = viewModel::onLastNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
        onToggleConfirmPasswordVisibility = viewModel::toggleConfirmPasswordVisibility,
        onTermsCheckboxClicked = viewModel::onTermsCheckboxClicked,
        onTermsClick = { viewModel.toggleTermsSheet(true) },
        onSignUpClick = viewModel::onSignUpClick,
        onNavigateToSignIn = onNavigateToSignIn,
    )
}
