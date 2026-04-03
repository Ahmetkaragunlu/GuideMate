package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.changepassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.common.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.toMessage
import com.ahmetkaragunlu.guidemate.domain.usecase.ChangePasswordUseCase
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.changepassword.model.ChangePasswordFormState
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.changepassword.model.ChangePasswordScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    private val _formState = MutableStateFlow(ChangePasswordFormState())
    val formState: StateFlow<ChangePasswordFormState> = _formState.asStateFlow()

    private val _screenState = MutableStateFlow(ChangePasswordScreenState())
    val screenState: StateFlow<ChangePasswordScreenState> = _screenState.asStateFlow()

    fun onCurrentPasswordChange(password: String) {
        _formState.update { it.copy(currentPassword = password.filterNot(Char::isWhitespace)) }
    }

    fun onNewPasswordChange(password: String) {
        _formState.update { it.copy(newPassword = password.filterNot(Char::isWhitespace)) }
    }

    fun onConfirmNewPasswordChange(password: String) {
        _formState.update { it.copy(confirmNewPassword = password.filterNot(Char::isWhitespace)) }
    }

    fun toggleCurrentPasswordVisibility() {
        _formState.update { it.copy(currentPasswordVisible = !it.currentPasswordVisible) }
    }

    fun toggleNewPasswordVisibility() {
        _formState.update { it.copy(newPasswordVisible = !it.newPasswordVisible) }
    }

    fun toggleConfirmNewPasswordVisibility() {
        _formState.update { it.copy(confirmNewPasswordVisible = !it.confirmNewPasswordVisible) }
    }

    fun onChangePasswordClick() {
        if (!canSubmit()) return
        submitChangePassword()
    }

    fun clearError() {
        _screenState.update { it.copy(errorMessage = null) }
    }

    fun clearSuccess() {
        _screenState.update { it.copy(successMessage = null) }
    }

    private fun canSubmit(): Boolean {
        val form = _formState.value
        val hasInputError =
            (!form.isCurrentPasswordValid && form.currentPassword.isNotEmpty()) ||
                (!form.isNewPasswordValid && form.newPassword.isNotEmpty()) ||
                (!form.isConfirmNewPasswordValid && form.confirmNewPassword.isNotEmpty())

        if (hasInputError) {
            _screenState.update {
                it.copy(errorMessage = resourceProvider.getString(R.string.error_fix_fields))
            }
            return false
        }

        if (form.currentPassword.isBlank() || form.newPassword.isBlank() || form.confirmNewPassword.isBlank()) {
            _screenState.update {
                it.copy(errorMessage = resourceProvider.getString(R.string.error_fill_all_fields))
            }
            return false
        }

        return true
    }

    private fun submitChangePassword() {
        viewModelScope.launch {
            val form = _formState.value
            when (
                val result =
                    changePasswordUseCase(
                        currentPassword = form.currentPassword,
                        newPassword = form.newPassword,
                        confirmPassword = form.confirmNewPassword,
                    )
            ) {
                is DataResult.Success -> {
                    _formState.value = ChangePasswordFormState()
                    _screenState.update { it.copy(successMessage = result.data) }
                }

                is DataResult.Error -> {
                    _screenState.update {
                        it.copy(errorMessage = result.error.toMessage(resourceProvider))
                    }
                }
            }
        }
    }
}
