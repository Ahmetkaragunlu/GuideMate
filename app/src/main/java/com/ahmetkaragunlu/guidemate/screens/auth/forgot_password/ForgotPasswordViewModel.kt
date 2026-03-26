package com.ahmetkaragunlu.guidemate.screens.auth.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.common.ResourceProvider
import com.ahmetkaragunlu.guidemate.data.remote.model.request.ForgotPasswordRequest
import com.ahmetkaragunlu.guidemate.domain.AuthRepository
import com.ahmetkaragunlu.guidemate.domain.usecase.ForgotPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _formState = MutableStateFlow(ForgotPasswordFormState())
    val formState: StateFlow<ForgotPasswordFormState> = _formState.asStateFlow()

    private val _screenState = MutableStateFlow(ForgotPasswordScreenState())
    val screenState: StateFlow<ForgotPasswordScreenState> = _screenState.asStateFlow()

    fun onFirstNameChange(value: String) {
        _formState.update { it.copy(firstName = value) }
    }

    fun onLastNameChange(value: String) {
        _formState.update { it.copy(lastName = value) }
    }

    fun onEmailChange(value: String) {
        _formState.update { it.copy(email = value.trim()) }
    }

    fun onSubmitClick() {
        if (checkErrors()) {
            sendResetLink()
        }
    }

    private fun sendResetLink() {
        viewModelScope.launch {
            val form = _formState.value
            when (val result = forgotPasswordUseCase(form.email, form.firstName, form.lastName)) {
                is DataResult.Success -> _screenState.update {
                    it.copy(
                        showSuccessDialog = true,
                        successMessage = result.data
                    )
                }

                is DataResult.Error -> _screenState.update { it.copy(errorMessage = result.message) }
            }
        }
    }

    private fun checkErrors(): Boolean {
        val form = _formState.value
        if (form.firstName.isBlank() || form.lastName.isBlank() || form.email.isBlank()) {
            _screenState.update { it.copy(errorMessage = resourceProvider.getString(R.string.error_fill_all_fields)) }
            return false
        }
        return true
    }

    fun clearError() {
        _screenState.update { it.copy(errorMessage = null) }
    }

    fun dismissSuccessDialog() {
        _screenState.update { it.copy(showSuccessDialog = false) }
    }
}