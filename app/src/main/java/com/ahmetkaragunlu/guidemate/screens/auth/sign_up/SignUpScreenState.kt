package com.ahmetkaragunlu.guidemate.screens.auth.sign_up

data class SignUpScreenState(
    val errorMessage: String? = null,
    val isRegistrationSuccess: Boolean = false,
    val isTermsAccepted: Boolean = false,
    val showTermsSheet: Boolean = false,
    val hasUserReadTerms: Boolean = false
)
