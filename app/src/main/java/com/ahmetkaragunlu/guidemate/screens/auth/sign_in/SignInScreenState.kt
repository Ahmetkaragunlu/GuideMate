package com.ahmetkaragunlu.guidemate.screens.auth.sign_in

data class SignInScreenState(
    val errorMessage: String? = null,
    val navigateToRoleSelection: Boolean = false
)