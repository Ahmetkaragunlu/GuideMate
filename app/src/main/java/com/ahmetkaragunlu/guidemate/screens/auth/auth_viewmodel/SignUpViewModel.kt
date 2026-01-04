package com.ahmetkaragunlu.guidemate.screens.auth.auth_viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.R
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
) : ViewModel() {


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


    fun inputFirstNameChange(name: String) {
        inputFirstName = name
    }

    fun onLastNameChange(lastName: String) {
        inputLastName = lastName
    }

    fun onEmailChange(email: String) {
        inputEmail = email
    }

    fun onPasswordChange(password: String) {
        inputPassword = password
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        inputConfirmPassword = confirmPassword
    }

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

    fun isValidPassWord() = inputPassword.isNotEmpty() && inputPassword.length >= 6
    fun isValidConfirmPassword() = inputPassword == inputConfirmPassword
    fun isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()


    fun shouldShowFirstNameError() = !isValidFirstName() && inputFirstName.isNotEmpty()
    fun shouldShowLastNameError() = !isValidLastName() && inputLastName.isNotEmpty()
    fun shouldShowPasswordError() = !isValidPassWord() && inputPassword.isNotEmpty()
    fun shouldShowConfirmPasswordError() =
        !isValidConfirmPassword() && inputConfirmPassword.isNotEmpty()

    fun shouldShowEmailError() = !isValidEmail() && inputEmail.isNotEmpty()


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