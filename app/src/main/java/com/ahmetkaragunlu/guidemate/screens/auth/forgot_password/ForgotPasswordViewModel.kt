package com.ahmetkaragunlu.guidemate.screens.auth.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.data.remote.model.request.ForgotPasswordRequest
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(ForgotPasswordFormState())
    val formState: StateFlow<ForgotPasswordFormState> = _formState.asStateFlow()

    private val _screenState = MutableStateFlow(ForgotPasswordScreenState())
    val screenState: StateFlow<ForgotPasswordScreenState> = _screenState.asStateFlow()

    // --- Input Changes ---
    fun onFirstNameChange(value: String) {
        _formState.update { it.copy(firstName = value) }
    }

    fun onLastNameChange(value: String) {
        _formState.update { it.copy(lastName = value) }
    }

    fun onEmailChange(value: String) {
        _formState.update { it.copy(email = value.trim()) }
    }

    // --- Form Submission ---
    fun onSubmitClick(onShowErrorToast: (Int) -> Unit) {
        if (checkErrors(onShowErrorToast)) {
            sendResetLink()
        }
    }

    private fun sendResetLink() {
        viewModelScope.launch {
            val request = ForgotPasswordRequest(
                firstName = _formState.value.firstName,
                lastName = _formState.value.lastName,
                email = _formState.value.email
            )

            when (val result = repository.forgotPassword(request)) {
                is DataResult.Success -> {
                    _screenState.update {
                        it.copy(
                            showSuccessDialog = true,
                            successMessage = result.data
                        )
                    }
                }
                is DataResult.Error -> {
                    _screenState.update {
                        it.copy(errorMessage = result.message)
                    }
                }
            }
        }
    }

    // --- Validation ---
    private fun checkErrors(onShowErrorToast: (Int) -> Unit): Boolean {
        val form = _formState.value
        if (form.firstName.isBlank() || form.lastName.isBlank() || form.email.isBlank()) {
            onShowErrorToast(R.string.error_fill_all_fields)
            return false
        }
        return true
    }

    // --- State Management ---
    fun clearError() {
        _screenState.update { it.copy(errorMessage = null) }
    }

    fun dismissSuccessDialog() {
        _screenState.update { it.copy(showSuccessDialog = false) }
    }
}