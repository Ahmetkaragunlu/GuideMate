package com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.model.ChangePasswordFormState
import com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.model.ChangePasswordScreenState
import com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.model.ChangePasswordValidationState
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton
import com.ahmetkaragunlu.guidemate.common.ui.components.EditTextField

@Composable
fun ChangePasswordContent(
    formState: ChangePasswordFormState,
    screenState: ChangePasswordScreenState,
    validation: ChangePasswordValidationState,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit,
    onToggleCurrentPasswordVisibility: () -> Unit,
    onToggleNewPasswordVisibility: () -> Unit,
    onToggleConfirmNewPasswordVisibility: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.Start,
    ) {
        ChangePasswordFieldsSection(
            formState = formState,
            currentPasswordErrorMessage = screenState.currentPasswordErrorMessage,
            newPasswordErrorMessage = screenState.newPasswordErrorMessage,
            validation = validation,
            onCurrentPasswordChange = onCurrentPasswordChange,
            onNewPasswordChange = onNewPasswordChange,
            onConfirmNewPasswordChange = onConfirmNewPasswordChange,
            onToggleCurrentPasswordVisibility = onToggleCurrentPasswordVisibility,
            onToggleNewPasswordVisibility = onToggleNewPasswordVisibility,
            onToggleConfirmNewPasswordVisibility = onToggleConfirmNewPasswordVisibility,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.change_password_helper_text),
            color = Color.Gray,
            style = MaterialTheme.typography.labelSmall,
            modifier =
                Modifier
                    .padding(horizontal = dimensionResource(R.dimen.spacing_tiny))
                    .align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.weight(1f))

        EditButton(
            text = R.string.update_password_action,
            onClick = onSubmit,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            isLoading = screenState.isLoading,
        )
    }
}

@Composable
private fun ChangePasswordFieldsSection(
    formState: ChangePasswordFormState,
    currentPasswordErrorMessage: String?,
    newPasswordErrorMessage: String?,
    validation: ChangePasswordValidationState,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit,
    onToggleCurrentPasswordVisibility: () -> Unit,
    onToggleNewPasswordVisibility: () -> Unit,
    onToggleConfirmNewPasswordVisibility: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        PasswordInputField(
            label = stringResource(R.string.current_password),
            value = formState.currentPassword,
            onValueChange = onCurrentPasswordChange,
            imeAction = ImeAction.Next,
            isPasswordVisible = formState.currentPasswordVisible,
            onTogglePasswordVisibility = onToggleCurrentPasswordVisibility,
            isError =
                (!validation.isCurrentPasswordValid && formState.currentPassword.isNotEmpty()) ||
                    currentPasswordErrorMessage != null,
            supportingText =
                if (!validation.isCurrentPasswordValid && formState.currentPassword.isNotEmpty()) {
                    R.string.password_error_message
                } else {
                    null
                },
            supportingTextValue = currentPasswordErrorMessage,
        )

        PasswordInputField(
            label = stringResource(R.string.new_password),
            value = formState.newPassword,
            onValueChange = onNewPasswordChange,
            imeAction = ImeAction.Next,
            isPasswordVisible = formState.newPasswordVisible,
            onTogglePasswordVisibility = onToggleNewPasswordVisibility,
            isError =
                (!validation.isNewPasswordValid && formState.newPassword.isNotEmpty()) ||
                    newPasswordErrorMessage != null,
            supportingText =
                if (!validation.isNewPasswordValid && formState.newPassword.isNotEmpty()) {
                    R.string.password_error_message
                } else {
                    null
                },
            supportingTextValue = newPasswordErrorMessage,
        )

        PasswordInputField(
            label = stringResource(R.string.new_password_repeat),
            value = formState.confirmNewPassword,
            onValueChange = onConfirmNewPasswordChange,
            imeAction = ImeAction.Done,
            isPasswordVisible = formState.confirmNewPasswordVisible,
            onTogglePasswordVisibility = onToggleConfirmNewPasswordVisibility,
            isError =
                !validation.isConfirmationValid && formState.confirmNewPassword.isNotEmpty(),
            supportingText =
                if (!validation.isConfirmationValid && formState.confirmNewPassword.isNotEmpty()) {
                    R.string.confirm_password_error_message
                } else {
                    null
                },
        )
    }
}

@Composable
private fun PasswordInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isError: Boolean,
    supportingText: Int?,
    supportingTextValue: String? = null,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = colorResource(R.color.text_color),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium,
            modifier =
                Modifier.padding(
                    bottom = 6.dp,
                    start = dimensionResource(R.dimen.spacing_tiny),
                ),
        )

        EditTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = R.string.password,
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = imeAction,
                ),
            visualTransformation =
                if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
            trailingIcon = {
                Icon(
                    imageVector =
                        if (isPasswordVisible) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.clickable(onClick = onTogglePasswordVisibility),
                )
            },
            isError = isError,
            supportingText = supportingText,
            supportingTextValue = supportingTextValue,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
