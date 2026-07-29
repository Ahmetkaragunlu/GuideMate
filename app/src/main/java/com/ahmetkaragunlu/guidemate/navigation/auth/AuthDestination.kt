package com.ahmetkaragunlu.guidemate.navigation.auth

import kotlinx.serialization.Serializable

object AuthDestination {
    @Serializable data object Onboarding

    @Serializable data object SignIn

    @Serializable data object SignUp

    @Serializable data object ForgotPassword
}

enum class AuthStartDestination {
    ONBOARDING,
    SIGN_IN,
}
