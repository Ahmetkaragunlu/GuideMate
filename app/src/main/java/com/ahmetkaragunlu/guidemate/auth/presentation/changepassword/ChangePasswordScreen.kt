package com.ahmetkaragunlu.guidemate.auth.presentation.changepassword

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.components.ChangePasswordContent
import com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.components.ChangePasswordSuccessDialog
import com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.components.HandleChangePasswordMessages

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
        validation = viewModel.validationState(),
        onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
        onNewPasswordChange = viewModel::onNewPasswordChange,
        onConfirmNewPasswordChange = viewModel::onConfirmNewPasswordChange,
        onToggleCurrentPasswordVisibility = viewModel::toggleCurrentPasswordVisibility,
        onToggleNewPasswordVisibility = viewModel::toggleNewPasswordVisibility,
        onToggleConfirmNewPasswordVisibility = viewModel::toggleConfirmNewPasswordVisibility,
        onSubmit = viewModel::onChangePasswordClick,
    )
}
