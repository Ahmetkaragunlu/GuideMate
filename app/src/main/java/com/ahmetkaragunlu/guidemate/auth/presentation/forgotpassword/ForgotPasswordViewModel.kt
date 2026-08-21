package com.ahmetkaragunlu.guidemate.auth.presentation.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.error.fieldMessage
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.auth.domain.repository.AuthRepository
import com.ahmetkaragunlu.guidemate.auth.domain.validation.EmailPolicy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val emailPolicy: EmailPolicy,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val _formState = MutableStateFlow(ForgotPasswordFormState())
    val formState: StateFlow<ForgotPasswordFormState> = _formState.asStateFlow()

    private val _screenState = MutableStateFlow(ForgotPasswordScreenState())
    val screenState: StateFlow<ForgotPasswordScreenState> = _screenState.asStateFlow()

    private var retryCountdownJob: Job? = null

    fun onEmailChange(value: String) {
        _formState.update { it.copy(email = value) }
        _screenState.update { it.copy(emailErrorMessage = null) }
    }

    fun isValidEmail(): Boolean = emailPolicy.isValid(_formState.value.email)

    fun onSubmitClick() {
        if (
            _screenState.value.isLoading ||
                _screenState.value.retryAfterSeconds > 0 ||
                !validateForm()
        ) {
            return
        }
        sendResetLink()
    }

    fun clearError() {
        _screenState.update { it.copy(errorMessage = null) }
    }

    fun dismissSuccessDialog() {
        _screenState.update { it.copy(showSuccessDialog = false) }
    }

    private fun sendResetLink() {
        viewModelScope.launch {
            _screenState.update { it.copy(isLoading = true) }
            when (val result = authRepository.forgotPassword(_formState.value.email)) {
                is DataResult.Success -> {
                    _screenState.update {
                        it.copy(
                            isLoading = false,
                            showSuccessDialog = true,
                        )
                    }
                }

                is DataResult.Error -> {
                    val retryAfterSeconds =
                        (result.error as? AppError.Backend)?.retryAfterSeconds
                    _screenState.update {
                        it.copy(
                            isLoading = false,
                            emailErrorMessage =
                                result.error.fieldMessage(FIELD_EMAIL, resourceProvider),
                            errorMessage = result.error.toMessage(resourceProvider),
                        )
                    }
                    retryAfterSeconds?.let(::startRetryCountdown)
                }
            }
        }
    }

    private fun startRetryCountdown(seconds: Long) {
        retryCountdownJob?.cancel()
        retryCountdownJob =
            viewModelScope.launch {
                for (remaining in seconds downTo 1) {
                    _screenState.update { it.copy(retryAfterSeconds = remaining) }
                    delay(1_000)
                }
                _screenState.update { it.copy(retryAfterSeconds = 0) }
            }
    }

    private fun validateForm(): Boolean {
        val email = _formState.value.email
        if (email.isBlank()) {
            _screenState.update {
                it.copy(errorMessage = resourceProvider.getString(R.string.error_fill_all_fields))
            }
            return false
        }
        if (!isValidEmail()) {
            _screenState.update {
                it.copy(emailErrorMessage = resourceProvider.getString(R.string.email_error_message))
            }
            return false
        }
        return true
    }

    private companion object {
        const val FIELD_EMAIL = "email"
    }
}
