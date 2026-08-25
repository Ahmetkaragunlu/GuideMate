package com.ahmetkaragunlu.guidemate.auth.presentation.signin.model

data class SignInFormState(
    val email: String = "",
    val password: String = "",
    val passwordVisibility: Boolean = false,
)
