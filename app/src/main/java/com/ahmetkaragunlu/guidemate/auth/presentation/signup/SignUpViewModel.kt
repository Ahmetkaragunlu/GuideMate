package com.ahmetkaragunlu.guidemate.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.error.fieldMessage
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.auth.domain.repository.AuthRepository
import com.ahmetkaragunlu.guidemate.auth.domain.validation.EmailPolicy
import com.ahmetkaragunlu.guidemate.auth.domain.validation.NumericPasswordPolicy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val emailPolicy: EmailPolicy,
    private val passwordPolicy: NumericPasswordPolicy,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val _formState = MutableStateFlow(SignUpFormState())
    val formState: StateFlow<SignUpFormState> = _formState.asStateFlow()

    private val _screenState = MutableStateFlow(SignUpScreenState())
    val screenState: StateFlow<SignUpScreenState> = _screenState.asStateFlow()

    fun inputFirstNameChange(name: String) {
        _formState.update { it.copy(firstName = name) }
        _screenState.update { it.copy(firstNameErrorMessage = null) }
    }

    fun onLastNameChange(lastName: String) {
        _formState.update { it.copy(lastName = lastName) }
        _screenState.update { it.copy(lastNameErrorMessage = null) }
    }

    fun onEmailChange(email: String) {
        _formState.update { it.copy(email = email) }
        _screenState.update { it.copy(emailErrorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _formState.update { it.copy(password = passwordPolicy.sanitize(password)) }
        _screenState.update { it.copy(passwordErrorMessage = null) }
    }

    fun onConfirmPasswordChange(confirm: String) {
        _formState.update { it.copy(confirmPassword = passwordPolicy.sanitize(confirm)) }
    }

    fun togglePasswordVisibility() {
        _formState.update { it.copy(passwordVisibility = !it.passwordVisibility) }
    }

    fun toggleConfirmPasswordVisibility() {
        _formState.update { it.copy(confirmPasswordVisibility = !it.confirmPasswordVisibility) }
    }

    fun markTermsAsRead() {
        _screenState.update { it.copy(hasUserReadTerms = true) }
    }

    fun acceptTerms() {
        _screenState.update { it.copy(isTermsAccepted = true, showTermsSheet = false) }
    }

    fun toggleTermsSheet(show: Boolean) {
        _screenState.update { it.copy(showTermsSheet = show) }
    }

    fun onTermsCheckboxClicked() {
        val current = _screenState.value
        if (!current.isTermsAccepted) {
            _screenState.update { it.copy(showTermsSheet = true) }
        } else {
            _screenState.update { it.copy(isTermsAccepted = false) }
        }
    }

    fun isValidFirstName(): Boolean {
        val value = _formState.value.firstName.trim()
        return value.length >= 3 && NAME_PATTERN.matches(value)
    }

    fun isValidLastName(): Boolean {
        val value = _formState.value.lastName.trim()
        return value.length >= 2 && NAME_PATTERN.matches(value)
    }

    fun isValidPassword(): Boolean = passwordPolicy.isValid(_formState.value.password)

    fun isValidConfirmPassword(): Boolean =
        _formState.value.password == _formState.value.confirmPassword

    fun isValidEmail(): Boolean = emailPolicy.isValid(_formState.value.email)

    fun onSignUpClick() {
        if (_screenState.value.isLoading || !validateForm()) return
        if (!_screenState.value.isTermsAccepted) {
            _screenState.update {
                it.copy(errorMessage = resourceProvider.getString(R.string.error_terms_required))
            }
            return
        }
        registerUser()
    }

    fun clearError() {
        _screenState.update { it.copy(errorMessage = null) }
    }

    fun resetRegistrationState() {
        _screenState.update { it.copy(isRegistrationSuccess = false) }
    }

    private fun registerUser() {
        viewModelScope.launch {
            _screenState.update { it.copy(isLoading = true) }
            val form = _formState.value
            when (
                val result =
                    authRepository.register(
                        form.firstName,
                        form.lastName,
                        form.email,
                        form.password,
                    )
            ) {
                is DataResult.Success -> {
                    _screenState.update {
                        it.copy(
                            isLoading = false,
                            isRegistrationSuccess = true,
                        )
                    }
                }

                is DataResult.Error -> {
                    _screenState.update {
                        it.copy(
                            isLoading = false,
                            firstNameErrorMessage =
                                result.error.fieldMessage(FIELD_FIRST_NAME, resourceProvider),
                            lastNameErrorMessage =
                                result.error.fieldMessage(FIELD_LAST_NAME, resourceProvider),
                            emailErrorMessage =
                                result.error.fieldMessage(FIELD_EMAIL, resourceProvider),
                            passwordErrorMessage =
                                result.error.fieldMessage(FIELD_PASSWORD, resourceProvider),
                            errorMessage = result.error.toMessage(resourceProvider),
                        )
                    }
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        val form = _formState.value
        if (
            form.firstName.isBlank() ||
                form.lastName.isBlank() ||
                form.email.isBlank() ||
                form.password.isBlank() ||
                form.confirmPassword.isBlank()
        ) {
            _screenState.update {
                it.copy(errorMessage = resourceProvider.getString(R.string.error_fill_all_fields))
            }
            return false
        }

        val hasInputError =
            !isValidFirstName() ||
                !isValidLastName() ||
                !isValidEmail() ||
                !isValidPassword() ||
                !isValidConfirmPassword()
        if (hasInputError) {
            _screenState.update {
                it.copy(errorMessage = resourceProvider.getString(R.string.error_fix_fields))
            }
            return false
        }
        return true
    }

    private companion object {
        val NAME_PATTERN =
            Regex("^[a-zA-ZğüşıöçĞÜŞİÖÇ]+(?: [a-zA-ZğüşıöçĞÜŞİÖÇ]+)*$")
        const val FIELD_FIRST_NAME = "firstName"
        const val FIELD_LAST_NAME = "lastName"
        const val FIELD_EMAIL = "email"
        const val FIELD_PASSWORD = "password"
    }
}
