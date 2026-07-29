package com.ahmetkaragunlu.guidemate.screens.auth.signin

data class SignInScreenState(
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val loginRetryAfterSeconds: Long = 0,
    val showVerificationDialog: Boolean = false,
    val verificationEmail: String? = null,
    val isResendingVerification: Boolean = false,
    val resendCooldownSeconds: Long = 0,
)
