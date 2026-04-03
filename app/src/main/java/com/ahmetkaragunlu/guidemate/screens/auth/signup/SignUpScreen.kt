package com.ahmetkaragunlu.guidemate.screens.auth.signup

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditTextField

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
    val isFirstNameValid = viewModel.isValidFirstName()
    val isLastNameValid = viewModel.isValidLastName()
    val isEmailValid = viewModel.isValidEmail()
    val isPasswordValid = viewModel.isValidPassWord()
    val isConfirmPasswordValid = viewModel.isValidConfirmPassword()

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

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(
                    bottom = dimensionResource(R.dimen.spacing_large),
                    top = dimensionResource(R.dimen.spacing_double_extra_large),
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SignUpHeader()
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_extra_large)))

        SignUpFormSection(
            formState = formState,
            isFirstNameValid = isFirstNameValid,
            isLastNameValid = isLastNameValid,
            isEmailValid = isEmailValid,
            isPasswordValid = isPasswordValid,
            isConfirmPasswordValid = isConfirmPasswordValid,
            onFirstNameChange = viewModel::inputFirstNameChange,
            onLastNameChange = viewModel::onLastNameChange,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
            onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
            onToggleConfirmPasswordVisibility = viewModel::toggleConfirmPasswordVisibility,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))

        TermsAgreementRow(
            isTermsAccepted = screenState.isTermsAccepted,
            onTermsCheckboxClicked = viewModel::onTermsCheckboxClicked,
            onTermsClick = { viewModel.toggleTermsSheet(true) },
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        EditButton(
            text = R.string.sign_up,
            onClick = viewModel::onSignUpClick
        )

        Spacer(modifier = Modifier.weight(1f))

        SignInFooterButton(onNavigateToSignIn = onNavigateToSignIn)
    }
}

@Composable
private fun SignUpHeader() {
    Text(
        text = stringResource(R.string.sign_up_title),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
    Text(
        text = stringResource(R.string.sign_up_subtitle),
        color = colorResource(R.color.text_color),
        style = MaterialTheme.typography.bodySmall,
    )
}

@Composable
private fun RegistrationSuccessDialog(
    onDismiss: () -> Unit,
) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TermsBottomSheet(
    sheetState: SheetState,
    hasUserReadTerms: Boolean,
    onDismiss: () -> Unit,
    onMarkTermsAsRead: () -> Unit,
    onAcceptTerms: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        val scrollState = rememberScrollState()
        val isCurrentlyAtBottom by remember {
            derivedStateOf {
                scrollState.maxValue == 0 || scrollState.value >= scrollState.maxValue - 50
            }
        }
        LaunchedEffect(isCurrentlyAtBottom) {
            if (isCurrentlyAtBottom) onMarkTermsAsRead()
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.terms_dialog_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier =
                    Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(scrollState),
            ) {
                Text(
                    text = stringResource(R.string.terms_and_conditions_full_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAcceptTerms,
                modifier = Modifier.fillMaxWidth(),
                enabled = hasUserReadTerms,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.brand_color),
                        disabledContainerColor = Color.Gray,
                        contentColor = Color.White,
                        disabledContentColor = Color.White,
                    ),
            ) {
                Text(
                    text =
                        stringResource(
                            if (hasUserReadTerms) R.string.terms_read_and_approve else R.string.terms_continue_reading,
                        ),
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SignUpFormSection(
    formState: SignUpFormState,
    isFirstNameValid: Boolean,
    isLastNameValid: Boolean,
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    isConfirmPasswordValid: Boolean,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
    ) {
        EditTextField(
            value = formState.firstName,
            onValueChange = onFirstNameChange,
            placeholder = R.string.name,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            isError = !isFirstNameValid && formState.firstName.isNotEmpty(),
            supportingText =
                if (!isFirstNameValid && formState.firstName.isNotEmpty()) {
                    R.string.name_error_message
                } else {
                    null
                },
        )
        EditTextField(
            value = formState.lastName,
            onValueChange = onLastNameChange,
            placeholder = R.string.last_name,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            isError = !isLastNameValid && formState.lastName.isNotEmpty(),
            supportingText =
                if (!isLastNameValid && formState.lastName.isNotEmpty()) {
                    R.string.last_name_error_message
                } else {
                    null
                },
        )
        EditTextField(
            value = formState.email,
            onValueChange = onEmailChange,
            placeholder = R.string.email,
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            isError = !isEmailValid && formState.email.isNotEmpty(),
            supportingText = if (!isEmailValid && formState.email.isNotEmpty()) R.string.email_error_message else null,
        )
        EditTextField(
            value = formState.password,
            onValueChange = onPasswordChange,
            placeholder = R.string.password,
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next,
                ),
            visualTransformation = if (formState.passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            isError = !isPasswordValid && formState.password.isNotEmpty(),
            trailingIcon = {
                Icon(
                    imageVector = if (formState.passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.clickable { onTogglePasswordVisibility() },
                )
            },
            supportingText =
                if (!isPasswordValid && formState.password.isNotEmpty()) {
                    R.string.password_error_message
                } else {
                    null
                },
        )
        EditTextField(
            value = formState.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            placeholder = R.string.confirm_password,
            visualTransformation = if (formState.confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done,
                ),
            trailingIcon = {
                Icon(
                    imageVector = if (formState.confirmPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.clickable { onToggleConfirmPasswordVisibility() },
                )
            },
            isError = !isConfirmPasswordValid && formState.confirmPassword.isNotEmpty(),
            supportingText =
                if (!isConfirmPasswordValid && formState.confirmPassword.isNotEmpty()) {
                    R.string.confirm_password_error_message
                } else {
                    null
                },
        )
    }
}

@Composable
private fun TermsAgreementRow(
    isTermsAccepted: Boolean,
    onTermsCheckboxClicked: () -> Unit,
    onTermsClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_extra_large)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isTermsAccepted,
            onCheckedChange = { onTermsCheckboxClicked() },
            colors =
                CheckboxDefaults.colors(
                    checkedColor = colorResource(R.color.brand_color),
                    uncheckedColor = colorResource(R.color.brand_color),
                ),
        )
        Text(
            text = stringResource(R.string.agree_terms_conditions),
            style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.Underline),
            color = colorResource(R.color.text_color),
            modifier =
                Modifier
                    .padding(top = 2.dp)
                    .clickable { onTermsClick() },
        )
    }
}

@Composable
private fun SignInFooterButton(
    onNavigateToSignIn: () -> Unit,
) {
    TextButton(onClick = onNavigateToSignIn) {
        Text(
            text =
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = colorResource(R.color.text_color))) {
                        append(stringResource(R.string.already_have_an_account))
                        append("  ")
                    }
                    withStyle(style = SpanStyle(color = colorResource(R.color.brand_color))) {
                        append(stringResource(R.string.login))
                    }
                },
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
