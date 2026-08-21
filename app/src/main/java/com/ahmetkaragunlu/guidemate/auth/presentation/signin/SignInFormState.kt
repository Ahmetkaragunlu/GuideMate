package com.ahmetkaragunlu.guidemate.auth.presentation.signin

data class SignInFormState(
    val email: String = "",
    val password: String = "",
    val passwordVisibility: Boolean = false,
)
