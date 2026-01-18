package com.ahmetkaragunlu.guidemate.screens.auth.forgot_password



data class ForgotPasswordScreenState(
    val showSuccessDialog: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)