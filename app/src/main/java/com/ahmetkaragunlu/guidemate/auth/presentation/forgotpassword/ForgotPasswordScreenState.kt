package com.ahmetkaragunlu.guidemate.auth.presentation.forgotpassword

data class ForgotPasswordScreenState(
    val showSuccessDialog: Boolean = false,
    val errorMessage: String? = null,
    val emailErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val retryAfterSeconds: Long = 0,
)
