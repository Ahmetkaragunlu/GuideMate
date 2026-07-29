package com.ahmetkaragunlu.guidemate.screens.common.changepassword

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditTextField
import com.ahmetkaragunlu.guidemate.screens.common.changepassword.model.ChangePasswordFormState
import com.ahmetkaragunlu.guidemate.screens.common.changepassword.model.ChangePasswordScreenState
import com.ahmetkaragunlu.guidemate.screens.common.changepassword.viewmodel.ChangePasswordViewModel

@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = hiltViewModel(),
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    HandleChangePasswordMessages(
        screenState = screenState,
        onErrorConsumed = viewModel::clearError,
    )

    if (screenState.showSuccessDialog) {
        BackHandler {}
        ChangePasswordSuccessDialog(onConfirm = viewModel::confirmSuccess)
    }

    ChangePasswordContent(
        formState = formState,
        screenState = screenState,
        isCurrentPasswordValid = viewModel.isCurrentPasswordValid(),
        isNewPasswordValid = viewModel.isNewPasswordValid(),
        isConfirmNewPasswordValid = viewModel.isConfirmNewPasswordValid(),
        onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
        onNewPasswordChange = viewModel::onNewPasswordChange,
        onConfirmNewPasswordChange = viewModel::onConfirmNewPasswordChange,
        onToggleCurrentPasswordVisibility = viewModel::toggleCurrentPasswordVisibility,
        onToggleNewPasswordVisibility = viewModel::toggleNewPasswordVisibility,
        onToggleConfirmNewPasswordVisibility = viewModel::toggleConfirmNewPasswordVisibility,
        onSubmit = viewModel::onChangePasswordClick,
    )
}

@Composable
private fun HandleChangePasswordMessages(
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
private fun ChangePasswordContent(
    formState: ChangePasswordFormState,
    screenState: ChangePasswordScreenState,
    isCurrentPasswordValid: Boolean,
    isNewPasswordValid: Boolean,
    isConfirmNewPasswordValid: Boolean,
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
            isCurrentPasswordValid = isCurrentPasswordValid,
            isNewPasswordValid = isNewPasswordValid,
            isConfirmNewPasswordValid = isConfirmNewPasswordValid,
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
                    .padding(horizontal = 4.dp)
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
    isCurrentPasswordValid: Boolean,
    isNewPasswordValid: Boolean,
    isConfirmNewPasswordValid: Boolean,
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
                (!isCurrentPasswordValid && formState.currentPassword.isNotEmpty()) ||
                    currentPasswordErrorMessage != null,
            supportingText =
                if (!isCurrentPasswordValid && formState.currentPassword.isNotEmpty()) {
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
                (!isNewPasswordValid && formState.newPassword.isNotEmpty()) ||
                    newPasswordErrorMessage != null,
            supportingText =
                if (!isNewPasswordValid && formState.newPassword.isNotEmpty()) {
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
                !isConfirmNewPasswordValid &&
                    formState.confirmNewPassword.isNotEmpty(),
            supportingText =
                if (!isConfirmNewPasswordValid && formState.confirmNewPassword.isNotEmpty()) {
                    R.string.confirm_password_error_message
                } else {
                    null
                },
        )
    }
}

@Composable
private fun ChangePasswordSuccessDialog(
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
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp),
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
