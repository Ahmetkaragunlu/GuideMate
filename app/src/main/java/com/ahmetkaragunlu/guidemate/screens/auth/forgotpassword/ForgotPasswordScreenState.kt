package com.ahmetkaragunlu.guidemate.screens.auth.forgotpassword

data class ForgotPasswordScreenState(
    val showSuccessDialog: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)
