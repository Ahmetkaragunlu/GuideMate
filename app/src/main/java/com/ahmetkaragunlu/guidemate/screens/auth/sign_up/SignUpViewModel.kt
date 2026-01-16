package com.ahmetkaragunlu.guidemate.screens.auth.sign_up

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RegisterRequest
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(SignUpFormState())
    val formState: StateFlow<SignUpFormState> = _formState.asStateFlow()

    private val _screenState = MutableStateFlow(SignUpScreenState())
    val screenState: StateFlow<SignUpScreenState> = _screenState.asStateFlow()

    // --- Input Changes ---
    fun inputFirstNameChange(name: String) {
        _formState.update { it.copy(firstName = name) }
    }

    fun onLastNameChange(lastName: String) {
        _formState.update { it.copy(lastName = lastName) }
    }

    fun onEmailChange(email: String) {
        _formState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        val clean = password.filter { !it.isWhitespace() }
        _formState.update { it.copy(password = clean) }
    }

    fun onConfirmPasswordChange(confirm: String) {
        val clean = confirm.filter { !it.isWhitespace() }
        _formState.update { it.copy(confirmPassword = clean) }
    }

    fun togglePasswordVisibility() {
        _formState.update { it.copy(passwordVisibility = !it.passwordVisibility) }
    }

    fun toggleConfirmPasswordVisibility() {
        _formState.update { it.copy(confirmPasswordVisibility = !it.confirmPasswordVisibility) }
    }

    // --- Terms & Conditions Logic ---
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
        if (!current.isTermsAccepted) _screenState.update { it.copy(showTermsSheet = true) }
        else _screenState.update { it.copy(isTermsAccepted = false) }
    }

    // --- Validation Controls ---
    fun isValidFirstName() = _formState.value.firstName.trim().length >= 3 &&
            _formState.value.firstName.matches(Regex("^[a-zA-ZğüşıöçĞÜŞİÖÇ]+(?: [a-zA-ZğüşıöçĞÜŞİÖÇ]+)*$"))

    fun isValidLastName() = _formState.value.lastName.trim().length >= 2 &&
            _formState.value.lastName.matches(Regex("^[a-zA-ZğüşıöçĞÜŞİÖÇ]+(?: [a-zA-ZğüşıöçĞÜŞİÖÇ]+)*$"))

    fun isValidPassWord() =
        _formState.value.password.length >= 6 && _formState.value.password.all { it.isDigit() }

    fun isValidConfirmPassword() = _formState.value.password == _formState.value.confirmPassword
    fun isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(_formState.value.email).matches()

    // --- Registration Process ---
    fun onSignUpClick(onShowErrorToast: (Int) -> Unit) {
        if (checkSignUpErrors(onShowErrorToast)) {
            if (!_screenState.value.isTermsAccepted) {
                onShowErrorToast(R.string.error_terms_required)
                return
            }
            registerUser()
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            val form = _formState.value
            val request = RegisterRequest(
                firstName = form.firstName,
                lastName = form.lastName,
                email = form.email,
                password = form.password
            )
            when (val result = repository.register(request)) {
                is DataResult.Success -> {
                    _screenState.update { it.copy(isRegistrationSuccess = true) }
                }

                is DataResult.Error -> {
                    _screenState.update { it.copy(errorMessage = result.message) }
                }
            }
        }
    }

    fun clearError() {
        _screenState.update { it.copy(errorMessage = null) }
    }

    fun resetRegistrationState() {
        _screenState.update { it.copy(isRegistrationSuccess = false) }
    }

    private fun checkSignUpErrors(onShowErrorToast: (Int) -> Unit): Boolean {
        val form = _formState.value
        val hasInputError = (!isValidFirstName() && form.firstName.isNotEmpty()) ||
                (!isValidLastName() && form.lastName.isNotEmpty()) ||
                (!isValidEmail() && form.email.isNotEmpty()) ||
                (!isValidPassWord() && form.password.isNotEmpty()) ||
                (!isValidConfirmPassword() && form.confirmPassword.isNotEmpty())

        if (hasInputError) {
            onShowErrorToast(R.string.error_fix_fields)
            return false
        }
        if (form.firstName.isBlank() || form.lastName.isBlank() || form.email.isBlank() ||
            form.password.isBlank() || form.confirmPassword.isBlank()
        ) {
            onShowErrorToast(R.string.error_fill_all_fields)
            return false
        }
        return true
    }
}