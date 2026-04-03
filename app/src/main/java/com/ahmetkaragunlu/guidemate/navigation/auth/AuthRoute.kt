package com.ahmetkaragunlu.guidemate.navigation.auth

enum class AuthRoute(
    val route: String,
) {
    OnboardingScreen(route = "OnboardingScreen"),
    RoleSelectionScreen(route = "RoleSelectionScreen"),
    SignInScreen(route = "SignInScreen"),
    SignUpScreen(route = "SignUpScreen"),
    ForgotPassWordScreen(route = "ForgotPasswordScreen"),
}
