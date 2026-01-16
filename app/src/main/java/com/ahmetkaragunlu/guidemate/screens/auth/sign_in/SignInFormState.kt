package com.ahmetkaragunlu.guidemate.screens.auth.sign_in

data class SignInFormState(
    val email: String = "",
    val password: String = "",
    val passwordVisibility: Boolean = false
)
