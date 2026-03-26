package com.ahmetkaragunlu.guidemate.screens.auth.sign_in

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.common.ResourceProvider
import com.ahmetkaragunlu.guidemate.domain.usecase.GoogleLoginUseCase
import com.ahmetkaragunlu.guidemate.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    val webClientId: String
        get() = resourceProvider.getString(R.string.default_web_client_id)

    private val _formState = MutableStateFlow(SignInFormState())
    val formState: StateFlow<SignInFormState> = _formState.asStateFlow()

    private val _screenState = MutableStateFlow(SignInScreenState())
    val screenState: StateFlow<SignInScreenState> = _screenState.asStateFlow()

    fun onEmailChange(email: String) {
        _formState.update { it.copy(email = email.trim()) }
    }

    fun onPasswordChange(password: String) {
        _formState.update { it.copy(password = password.filter { !it.isWhitespace() }) }
    }

    fun togglePasswordVisibility() {
        _formState.update { it.copy(passwordVisibility = !it.passwordVisibility) }
    }

    fun isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(_formState.value.email).matches()

    fun isValidPassword(): Boolean {
        val password = _formState.value.password
        return password.length >= 6 && password.all { it.isDigit() }
    }

    fun onSignInClick(onShowErrorToast: (Int) -> Unit) {
        if (checkSignInErrors(onShowErrorToast)) loginUser()
    }

    private fun loginUser() {
        viewModelScope.launch {
            when (val result = loginUseCase(_formState.value.email, _formState.value.password)) {
                is DataResult.Success -> {
                    _screenState.update { it.copy(navigateToRoleSelection = true) }
                }

                is DataResult.Error -> {
                    _screenState.update { it.copy(errorMessage = result.message) }
                }
            }
        }
    }

    fun onGoogleSignInSuccess(idToken: String) {
        viewModelScope.launch {
            when (val result = googleLoginUseCase(idToken)) {
                is DataResult.Success -> {
                    _screenState.update { it.copy(navigateToRoleSelection = true) }
                }

                is DataResult.Error -> {
                    _screenState.update { it.copy(errorMessage = result.message) }
                }
            }
        }
    }

    fun clearError() = _screenState.update { it.copy(errorMessage = null) }

    fun resetNavigationState() = _screenState.update { it.copy(navigateToRoleSelection = false) }

    private fun checkSignInErrors(onShowErrorToast: (Int) -> Unit): Boolean {
        val form = _formState.value
        if ((!isValidEmail() && form.email.isNotEmpty()) || (!isValidPassword() && form.password.isNotEmpty())) {
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