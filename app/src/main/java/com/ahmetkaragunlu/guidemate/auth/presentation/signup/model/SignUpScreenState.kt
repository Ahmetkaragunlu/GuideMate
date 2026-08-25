package com.ahmetkaragunlu.guidemate.auth.presentation.signup.model

data class SignUpScreenState(
    val errorMessage: String? = null,
    val firstNameErrorMessage: String? = null,
    val lastNameErrorMessage: String? = null,
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val isRegistrationSuccess: Boolean = false,
    val isTermsAccepted: Boolean = false,
    val showTermsSheet: Boolean = false,
    val hasUserReadTerms: Boolean = false,
)
