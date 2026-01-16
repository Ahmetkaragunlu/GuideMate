package com.ahmetkaragunlu.guidemate.screens.auth.sign_in

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.data.remote.model.request.GoogleLoginRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.LoginRequest
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(SignInFormState())
    val formState: StateFlow<SignInFormState> = _formState.asStateFlow()

    private val _screenState = MutableStateFlow(SignInScreenState())
    val screenState: StateFlow<SignInScreenState> = _screenState.asStateFlow()

    // --- Input Changes ---
    fun onEmailChange(email: String) {
        _formState.update { it.copy(email = email.trim()) }
    }

    fun onPasswordChange(password: String) {
        val clean = password.filter { !it.isWhitespace() }
        _formState.update { it.copy(password = clean) }
    }

    fun togglePasswordVisibility() {
        _formState.update { it.copy(passwordVisibility = !it.passwordVisibility) }
    }

    // --- Validation ---
    fun isValidEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(_formState.value.email).matches()
    }


    fun isValidPassword(): Boolean {
        val password = _formState.value.password
        return password.length >= 6 && password.all { it.isDigit() }
    }

    // --- Login Process ---
    fun onSignInClick(onShowErrorToast: (Int) -> Unit) {
        if (checkSignInErrors(onShowErrorToast)) {
            loginUser()
        }
    }

    private fun loginUser() {
        viewModelScope.launch {
            val request = LoginRequest(
                email = _formState.value.email,
                password = _formState.value.password
            )

            when (val result = repository.login(request)) {
                is DataResult.Success -> {
                    _screenState.update { it.copy(navigateToRoleSelection = true) }
                }

                is DataResult.Error -> {
                    _screenState.update { it.copy(errorMessage = result.message) }
                }
            }
        }
    }

    // --- Google Login ---
    fun onGoogleSignInSuccess(idToken: String) {
        viewModelScope.launch {
            val request = GoogleLoginRequest(idToken = idToken)

            when (val result = repository.googleLogin(request)) {
                is DataResult.Success -> {
                    _screenState.update { it.copy(navigateToRoleSelection = true) }
                }

                is DataResult.Error -> {
                    _screenState.update { it.copy(errorMessage = result.message) }
                }
            }
        }
    }

    // --- Error & Navigation Management ---
    fun clearError() {
        _screenState.update { it.copy(errorMessage = null) }
    }

    fun resetNavigationState() {
        _screenState.update { it.copy(navigateToRoleSelection = false) }
    }

    // --- Validation Check ---
    private fun checkSignInErrors(onShowErrorToast: (Int) -> Unit): Boolean {
        val form = _formState.value

        val hasInputError = (!isValidEmail() && form.email.isNotEmpty()) ||
                (!isValidPassword() && form.password.isNotEmpty())

        if (hasInputError) {
            onShowErrorToast(R.string.error_fix_fields)
            return false
        }

        if (form.email.isBlank() || form.password.isBlank()) {
            onShowErrorToast(R.string.error_fill_all_fields)
            return false
        }
        return true
    }
}