package com.ahmetkaragunlu.guidemate.screens.auth.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.data.remote.model.RegisterRequest
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.ahmetkaragunlu.guidemate.core.result.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    // ----------------------------------------------------------------
    // UI State & Inputs
    // ----------------------------------------------------------------
    var inputFirstName by mutableStateOf("")
        private set
    var inputLastName by mutableStateOf("")
        private set
    var inputEmail by mutableStateOf("")
        private set
    var inputPassword by mutableStateOf("")
        private set
    var inputConfirmPassword by mutableStateOf("")
        private set

    var passwordVisibility by mutableStateOf(false)
    var confirmPasswordVisibility by mutableStateOf(false)

    var errorMessage by mutableStateOf<String?>(null)
    var isRegistrationSuccess by mutableStateOf(false)

    // Terms & Conditions States
    var isTermsAccepted by mutableStateOf(false)
    var showTermsSheet by mutableStateOf(false)
    var hasUserReadTerms by mutableStateOf(false)

    // ----------------------------------------------------------------
    // Input Events
    // ----------------------------------------------------------------
    fun inputFirstNameChange(name: String) { inputFirstName = name }
    fun onLastNameChange(lastName: String) { inputLastName = lastName }
    fun onEmailChange(email: String) { inputEmail = email }

    fun onPasswordChange(password: String) {
        // Remove whitespace for cleaner input
        inputPassword = password.filter { !it.isWhitespace() }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        inputConfirmPassword = confirmPassword.filter { !it.isWhitespace() }
    }

    // ----------------------------------------------------------------
    // Terms & Conditions Logic
    // ----------------------------------------------------------------
    fun markTermsAsRead() {
        hasUserReadTerms = true
    }

    fun acceptTerms() {
        isTermsAccepted = true
        showTermsSheet = false
    }

    // ----------------------------------------------------------------
    // Validation Logic
    // ----------------------------------------------------------------
    fun isValidFirstName(): Boolean {
        val text = inputFirstName.trim()
        if (text.length < 3) return false
        val nameRegex = Regex("^[a-zA-ZğüşıöçĞÜŞİÖÇ]+(?: [a-zA-ZğüşıöçĞÜŞİÖÇ]+)*$")
        return text.matches(nameRegex)
    }

    fun isValidLastName(): Boolean {
        val text = inputLastName.trim()
        if (text.length < 2) return false
        val lastNameRegex = Regex("^[a-zA-ZğüşıöçĞÜŞİÖÇ]+(?: [a-zA-ZğüşıöçĞÜŞİÖÇ]+)*$")
        return text.matches(lastNameRegex)
    }

    fun isValidPassWord(): Boolean {
        return inputPassword.isNotEmpty() &&
                inputPassword.length >= 6 &&
                inputPassword.all { it.isDigit() }
    }

    fun isValidConfirmPassword() = inputPassword == inputConfirmPassword
    fun isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()

    // ----------------------------------------------------------------
    // UI Error States
    // ----------------------------------------------------------------
    fun shouldShowFirstNameError() = !isValidFirstName() && inputFirstName.isNotEmpty()
    fun shouldShowLastNameError() = !isValidLastName() && inputLastName.isNotEmpty()
    fun shouldShowPasswordError() = !isValidPassWord() && inputPassword.isNotEmpty()
    fun shouldShowConfirmPasswordError() = !isValidConfirmPassword() && inputConfirmPassword.isNotEmpty()
    fun shouldShowEmailError() = !isValidEmail() && inputEmail.isNotEmpty()

    // ----------------------------------------------------------------
    // Registration Logic
    // ----------------------------------------------------------------
    fun onSignUpClick(onShowErrorToast: (Int) -> Unit) {
        if (checkSignUpErrors(onShowErrorToast)) {
            if (!isTermsAccepted) {
                onShowErrorToast(R.string.error_terms_required)
                return
            }
            registerUser()
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            val request = RegisterRequest(
                firstName = inputFirstName,
                lastName = inputLastName,
                email = inputEmail,
                password = inputPassword
            )
            when (val result = repository.register(request)) {
                is Result.Success -> {
                    isRegistrationSuccess = true
                }
                is Result.Error -> {
                    errorMessage = result.message
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // Helper Methods
    // ----------------------------------------------------------------
    fun clearError() {
        errorMessage = null
    }

    fun resetRegistrationState() {
        isRegistrationSuccess = false
    }

    fun checkSignUpErrors(onShowErrorToast: (Int) -> Unit): Boolean {
        val hasInputError = shouldShowFirstNameError() || shouldShowLastNameError() ||
                shouldShowEmailError() || shouldShowPasswordError() ||
                shouldShowConfirmPasswordError()

        if (hasInputError) {
            onShowErrorToast(R.string.error_fix_fields)
            return false
        }

        val hasEmptyField = inputFirstName.isBlank() || inputLastName.isBlank() ||
                inputEmail.isBlank() || inputPassword.isBlank() ||
                inputConfirmPassword.isBlank()

        if (hasEmptyField) {
            onShowErrorToast(R.string.error_fill_all_fields)
            return false
        }

        return true
    }
}