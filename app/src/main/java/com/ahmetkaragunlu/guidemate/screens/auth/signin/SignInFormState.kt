package com.ahmetkaragunlu.guidemate.screens.auth.signin

data class SignInFormState(
    val email: String = "",
    val password: String = "",
    val passwordVisibility: Boolean = false,
)
