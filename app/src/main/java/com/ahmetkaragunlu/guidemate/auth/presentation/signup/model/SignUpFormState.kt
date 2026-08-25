package com.ahmetkaragunlu.guidemate.auth.presentation.signup.model

data class SignUpFormState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisibility: Boolean = false,
    val confirmPasswordVisibility: Boolean = false,
)
