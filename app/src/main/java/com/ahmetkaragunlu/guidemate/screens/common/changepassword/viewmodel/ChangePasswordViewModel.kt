package com.ahmetkaragunlu.guidemate.screens.common.changepassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.common.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.fieldMessage
import com.ahmetkaragunlu.guidemate.common.toMessage
import com.ahmetkaragunlu.guidemate.domain.usecase.ChangePasswordUseCase
import com.ahmetkaragunlu.guidemate.domain.usecase.ClearSessionUseCase
import com.ahmetkaragunlu.guidemate.domain.validation.NumericPasswordPolicy
import com.ahmetkaragunlu.guidemate.screens.common.changepassword.model.ChangePasswordFormState
import com.ahmetkaragunlu.guidemate.screens.common.changepassword.model.ChangePasswordScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val clearSessionUseCase: ClearSessionUseCase,
    private val passwordPolicy: NumericPasswordPolicy,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val _formState = MutableStateFlow(ChangePasswordFormState())
    val formState: StateFlow<ChangePasswordFormState> = _formState.asStateFlow()

    private val _screenState = MutableStateFlow(ChangePasswordScreenState())
    val screenState: StateFlow<ChangePasswordScreenState> = _screenState.asStateFlow()

    fun onCurrentPasswordChange(password: String) {
        _formState.update {
            it.copy(currentPassword = passwordPolicy.sanitize(password))
        }
        _screenState.update { it.copy(currentPasswordErrorMessage = null) }
    }

    fun onNewPasswordChange(password: String) {
        _formState.update {
            it.copy(newPassword = passwordPolicy.sanitize(password))
        }
        _screenState.update { it.copy(newPasswordErrorMessage = null) }
    }

    fun onConfirmNewPasswordChange(password: String) {
        _formState.update {
            it.copy(confirmNewPassword = passwordPolicy.sanitize(password))
        }
    }

    fun toggleCurrentPasswordVisibility() {
        _formState.update { it.copy(currentPasswordVisible = !it.currentPasswordVisible) }
    }

    fun toggleNewPasswordVisibility() {
        _formState.update { it.copy(newPasswordVisible = !it.newPasswordVisible) }
    }

    fun toggleConfirmNewPasswordVisibility() {
        _formState.update {
            it.copy(confirmNewPasswordVisible = !it.confirmNewPasswordVisible)
        }
    }

    fun isCurrentPasswordValid(): Boolean =
        passwordPolicy.isValid(_formState.value.currentPassword)

    fun isNewPasswordValid(): Boolean =
        passwordPolicy.isValid(_formState.value.newPassword)

    fun isConfirmNewPasswordValid(): Boolean {
        val form = _formState.value
        return form.newPassword == form.confirmNewPassword
    }

    fun onChangePasswordClick() {
        if (_screenState.value.isLoading || !validateForm()) return
        submitChangePassword()
    }

    fun clearError() {
        _screenState.update { it.copy(errorMessage = null) }
    }

    fun confirmSuccess() {
        if (!_screenState.value.showSuccessDialog) return
        _screenState.update { it.copy(showSuccessDialog = false) }
        viewModelScope.launch {
            clearSessionUseCase()
        }
    }

    private fun validateForm(): Boolean {
        val form = _formState.value
        if (
            form.currentPassword.isBlank() ||
                form.newPassword.isBlank() ||
                form.confirmNewPassword.isBlank()
        ) {
            _screenState.update {
                it.copy(errorMessage = resourceProvider.getString(R.string.error_fill_all_fields))
            }
            return false
        }

        if (
            !isCurrentPasswordValid() ||
                !isNewPasswordValid() ||
                !isConfirmNewPasswordValid()
        ) {
            _screenState.update {
                it.copy(errorMessage = resourceProvider.getString(R.string.error_fix_fields))
            }
            return false
        }
        return true
    }

    private fun submitChangePassword() {
        viewModelScope.launch {
            _screenState.update { it.copy(isLoading = true) }
            val form = _formState.value
            when (
                val result =
                    changePasswordUseCase(
                        currentPassword = form.currentPassword,
                        newPassword = form.newPassword,
                    )
            ) {
                is DataResult.Success -> {
                    _formState.value = ChangePasswordFormState()
                    _screenState.update {
                        it.copy(
                            isLoading = false,
                            showSuccessDialog = true,
                        )
                    }
                }

                is DataResult.Error -> {
                    _screenState.update {
                        it.copy(
                            isLoading = false,
                            currentPasswordErrorMessage =
                                result.error.fieldMessage(FIELD_CURRENT_PASSWORD, resourceProvider),
                            newPasswordErrorMessage =
                                result.error.fieldMessage(FIELD_NEW_PASSWORD, resourceProvider),
                            errorMessage = result.error.toMessage(resourceProvider),
                        )
                    }
                }
            }
        }
    }

    private companion object {
        const val FIELD_CURRENT_PASSWORD = "currentPassword"
        const val FIELD_NEW_PASSWORD = "newPassword"
    }
}
