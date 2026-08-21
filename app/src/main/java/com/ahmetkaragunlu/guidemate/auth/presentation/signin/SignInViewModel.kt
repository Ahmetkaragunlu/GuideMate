package com.ahmetkaragunlu.guidemate.auth.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.error.fieldMessage
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.auth.domain.repository.AuthRepository
import com.ahmetkaragunlu.guidemate.auth.domain.validation.EmailPolicy
import com.ahmetkaragunlu.guidemate.auth.domain.validation.NumericPasswordPolicy
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
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val emailPolicy: EmailPolicy,
    private val passwordPolicy: NumericPasswordPolicy,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    val webClientId: String
        get() = resourceProvider.getString(R.string.default_web_client_id)

    private val _formState = MutableStateFlow(SignInFormState())
    val formState: StateFlow<SignInFormState> = _formState.asStateFlow()

    private val _screenState = MutableStateFlow(SignInScreenState())
    val screenState: StateFlow<SignInScreenState> = _screenState.asStateFlow()

    private var resendCooldownJob: Job? = null
    private var loginCooldownJob: Job? = null

    fun onEmailChange(email: String) {
        _formState.update { it.copy(email = email) }
        _screenState.update { it.copy(emailErrorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _formState.update { it.copy(password = passwordPolicy.sanitize(password)) }
        _screenState.update { it.copy(passwordErrorMessage = null) }
    }

    fun togglePasswordVisibility() {
        _formState.update { it.copy(passwordVisibility = !it.passwordVisibility) }
    }

    fun isValidEmail(): Boolean = emailPolicy.isValid(_formState.value.email)

    fun isValidPassword(): Boolean = passwordPolicy.isValid(_formState.value.password)

    fun onSignInClick() {
        if (
            _screenState.value.isLoading ||
                _screenState.value.loginRetryAfterSeconds > 0 ||
                !validateForm()
        ) {
            return
        }
        authenticate(verificationEmail = emailPolicy.normalize(_formState.value.email)) {
            authRepository.login(_formState.value.email, _formState.value.password)
        }
    }

    fun onGoogleSignInStarted() {
        _screenState.update { it.copy(isLoading = true) }
    }

    fun onGoogleSignInCancelled() {
        _screenState.update { it.copy(isLoading = false) }
    }

    fun onGoogleSignInFailure() {
        _screenState.update {
            it.copy(
                isLoading = false,
                errorMessage = resourceProvider.getString(R.string.error_google_login_failed),
            )
        }
    }

    fun onGoogleSignInSuccess(idToken: String) {
        authenticate { authRepository.googleLogin(idToken) }
    }

    fun resendVerificationEmail() {
        val state = _screenState.value
        val email = state.verificationEmail ?: return
        if (state.isResendingVerification || state.resendCooldownSeconds > 0) return

        viewModelScope.launch {
            _screenState.update { it.copy(isResendingVerification = true) }
            when (val result = authRepository.resendVerification(email)) {
                is DataResult.Success -> {
                    _screenState.update {
                        it.copy(
                            isResendingVerification = false,
                            infoMessage =
                                resourceProvider.getString(R.string.verification_email_sent),
                        )
                    }
                    startResendCooldown(DEFAULT_RESEND_COOLDOWN_SECONDS)
                }

                is DataResult.Error -> {
                    val retryAfterSeconds =
                        (result.error as? AppError.Backend)?.retryAfterSeconds
                    _screenState.update {
                        it.copy(
                            isResendingVerification = false,
                            errorMessage = result.error.toMessage(resourceProvider),
                        )
                    }
                    retryAfterSeconds?.let(::startResendCooldown)
                }
            }
        }
    }

    fun dismissVerificationDialog() {
        _screenState.update { it.copy(showVerificationDialog = false) }
    }

    fun clearError() {
        _screenState.update { it.copy(errorMessage = null) }
    }

    fun clearInfoMessage() {
        _screenState.update { it.copy(infoMessage = null) }
    }

    private fun authenticate(
        verificationEmail: String? = null,
        request: suspend () -> DataResult<*>,
    ) {
        viewModelScope.launch {
            _screenState.update { it.copy(isLoading = true) }
            when (val result = request()) {
                is DataResult.Success -> {
                    _screenState.update { it.copy(isLoading = false) }
                }

                is DataResult.Error -> {
                    handleAuthenticationError(result.error, verificationEmail)
                }
            }
        }
    }

    private fun handleAuthenticationError(
        error: AppError,
        verificationEmail: String?,
    ) {
        val backendError = error as? AppError.Backend
        if (
            backendError?.code == BackendErrorCode.ACCOUNT_PENDING_VERIFICATION &&
                !verificationEmail.isNullOrBlank()
        ) {
            _screenState.update {
                it.copy(
                    isLoading = false,
                    showVerificationDialog = true,
                    verificationEmail = verificationEmail,
                )
            }
            return
        }

        backendError?.retryAfterSeconds?.let(::startLoginCooldown)
        _screenState.update {
            it.copy(
                isLoading = false,
                emailErrorMessage = error.fieldMessage(FIELD_EMAIL, resourceProvider),
                passwordErrorMessage = error.fieldMessage(FIELD_PASSWORD, resourceProvider),
                errorMessage = error.toMessage(resourceProvider),
            )
        }
    }

    private fun validateForm(): Boolean {
        val form = _formState.value
        if (form.email.isBlank() || form.password.isBlank()) {
            _screenState.update {
                it.copy(errorMessage = resourceProvider.getString(R.string.error_fill_all_fields))
            }
            return false
        }
        if (!isValidEmail() || !isValidPassword()) {
            _screenState.update {
                it.copy(errorMessage = resourceProvider.getString(R.string.error_fix_fields))
            }
            return false
        }
        return true
    }

    private fun startResendCooldown(seconds: Long) {
        resendCooldownJob?.cancel()
        resendCooldownJob =
            viewModelScope.launch {
                for (remaining in seconds downTo 1) {
                    _screenState.update { it.copy(resendCooldownSeconds = remaining) }
                    delay(1_000)
                }
                _screenState.update { it.copy(resendCooldownSeconds = 0) }
            }
    }

    private fun startLoginCooldown(seconds: Long) {
        loginCooldownJob?.cancel()
        loginCooldownJob =
            viewModelScope.launch {
                for (remaining in seconds downTo 1) {
                    _screenState.update { it.copy(loginRetryAfterSeconds = remaining) }
                    delay(1_000)
                }
                _screenState.update { it.copy(loginRetryAfterSeconds = 0) }
            }
    }

    private companion object {
        const val FIELD_EMAIL = "email"
        const val FIELD_PASSWORD = "password"
        const val DEFAULT_RESEND_COOLDOWN_SECONDS = 60L
    }
}
