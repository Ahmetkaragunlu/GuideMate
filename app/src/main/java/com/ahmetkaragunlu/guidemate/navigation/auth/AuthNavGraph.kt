package com.ahmetkaragunlu.guidemate.navigation.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ahmetkaragunlu.guidemate.navigation.RootDestination
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.screens.auth.forgotpassword.ForgotPasswordScreen
import com.ahmetkaragunlu.guidemate.screens.auth.onboarding.OnboardingScreen
import com.ahmetkaragunlu.guidemate.screens.auth.signin.SignInScreen
import com.ahmetkaragunlu.guidemate.screens.auth.signup.SignUpScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    startDestination: AuthStartDestination,
    onOnboardingCompleted: () -> Unit,
) {
    navigation<RootDestination.Auth>(
        startDestination = startDestination.toDestination(),
    ) {
        composable<AuthDestination.Onboarding> {
            OnboardingScreen(
                onboardingCompleted = {
                    onOnboardingCompleted()
                    navController.navigate(AuthDestination.SignIn) {
                        popUpTo(AuthDestination.Onboarding) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable<AuthDestination.SignIn> {
            SignInScreen(
                onNavigateToSignUp = {
                    navController.navigateTo(AuthDestination.SignUp)
                },
                onNavigateToForgotPassword = {
                    navController.navigateTo(AuthDestination.ForgotPassword)
                },
            )
        }
        composable<AuthDestination.SignUp> {
            SignUpScreen(
                onNavigateToSignIn = {
                    navController.popBackStack(
                        route = AuthDestination.SignIn,
                        inclusive = false,
                    )
                },
            )
        }
        composable<AuthDestination.ForgotPassword> {
            ForgotPasswordScreen(
                onNavigateToSignIn = {
                    navController.popBackStack(
                        route = AuthDestination.SignIn,
                        inclusive = false,
                    )
                },
            )
        }
    }
}

private fun AuthStartDestination.toDestination(): Any =
    when (this) {
        AuthStartDestination.ONBOARDING -> AuthDestination.Onboarding
        AuthStartDestination.SIGN_IN -> AuthDestination.SignIn
    }
