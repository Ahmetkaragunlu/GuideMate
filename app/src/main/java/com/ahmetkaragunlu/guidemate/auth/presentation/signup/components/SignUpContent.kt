package com.ahmetkaragunlu.guidemate.auth.presentation.signup.components

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.auth.presentation.signup.model.SignUpFormState
import com.ahmetkaragunlu.guidemate.auth.presentation.signup.model.SignUpScreenState
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton
import com.ahmetkaragunlu.guidemate.common.ui.components.EditTextField

@Composable
internal fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    formState: SignUpFormState,
    screenState: SignUpScreenState,
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
    onTermsCheckboxClicked: () -> Unit,
    onTermsClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onNavigateToSignIn: () -> Unit,
) {
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
            firstNameErrorMessage = screenState.firstNameErrorMessage,
            lastNameErrorMessage = screenState.lastNameErrorMessage,
            emailErrorMessage = screenState.emailErrorMessage,
            passwordErrorMessage = screenState.passwordErrorMessage,
            onFirstNameChange = onFirstNameChange,
            onLastNameChange = onLastNameChange,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onConfirmPasswordChange = onConfirmPasswordChange,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            onToggleConfirmPasswordVisibility = onToggleConfirmPasswordVisibility,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
        TermsAgreementRow(
            isTermsAccepted = screenState.isTermsAccepted,
            onTermsCheckboxClicked = onTermsCheckboxClicked,
            onTermsClick = onTermsClick,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        EditButton(
            text = R.string.sign_up,
            onClick = onSignUpClick,
            isLoading = screenState.isLoading,
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
private fun SignUpFormSection(
    formState: SignUpFormState,
    isFirstNameValid: Boolean,
    isLastNameValid: Boolean,
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    isConfirmPasswordValid: Boolean,
    firstNameErrorMessage: String?,
    lastNameErrorMessage: String?,
    emailErrorMessage: String?,
    passwordErrorMessage: String?,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
    ) {
        EditTextField(
            value = formState.firstName,
            onValueChange = onFirstNameChange,
            placeholder = R.string.name,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            isError = (!isFirstNameValid && formState.firstName.isNotEmpty()) || firstNameErrorMessage != null,
            supportingText =
                if (!isFirstNameValid && formState.firstName.isNotEmpty()) {
                    R.string.name_error_message
                } else {
                    null
                },
            supportingTextValue = firstNameErrorMessage,
        )
        EditTextField(
            value = formState.lastName,
            onValueChange = onLastNameChange,
            placeholder = R.string.last_name,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            isError = (!isLastNameValid && formState.lastName.isNotEmpty()) || lastNameErrorMessage != null,
            supportingText =
                if (!isLastNameValid && formState.lastName.isNotEmpty()) {
                    R.string.last_name_error_message
                } else {
                    null
                },
            supportingTextValue = lastNameErrorMessage,
        )
        EditTextField(
            value = formState.email,
            onValueChange = onEmailChange,
            placeholder = R.string.email,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            isError = (!isEmailValid && formState.email.isNotEmpty()) || emailErrorMessage != null,
            supportingText =
                if (!isEmailValid && formState.email.isNotEmpty()) R.string.email_error_message else null,
            supportingTextValue = emailErrorMessage,
        )
        EditTextField(
            value = formState.password,
            onValueChange = onPasswordChange,
            placeholder = R.string.password,
            keyboardOptions =
                KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Next),
            visualTransformation =
                if (formState.passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            isError = (!isPasswordValid && formState.password.isNotEmpty()) || passwordErrorMessage != null,
            trailingIcon = {
                Icon(
                    imageVector =
                        if (formState.passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
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
            supportingTextValue = passwordErrorMessage,
        )
        EditTextField(
            value = formState.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            placeholder = R.string.confirm_password,
            visualTransformation =
                if (formState.confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions =
                KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
            trailingIcon = {
                Icon(
                    imageVector =
                        if (formState.confirmPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
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
            modifier = Modifier.padding(top = 2.dp).clickable { onTermsClick() },
        )
    }
}

@Composable
private fun SignInFooterButton(onNavigateToSignIn: () -> Unit) {
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
