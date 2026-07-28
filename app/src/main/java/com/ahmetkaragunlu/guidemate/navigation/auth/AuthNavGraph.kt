package com.ahmetkaragunlu.guidemate.navigation.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ahmetkaragunlu.guidemate.navigation.RootDestination
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.navigation.switchRoot
import com.ahmetkaragunlu.guidemate.screens.auth.forgotpassword.ForgotPasswordScreen
import com.ahmetkaragunlu.guidemate.screens.auth.onboarding.OnboardingScreen
import com.ahmetkaragunlu.guidemate.screens.auth.roleselection.RoleSelectionScreen
import com.ahmetkaragunlu.guidemate.screens.auth.signin.SignInScreen
import com.ahmetkaragunlu.guidemate.screens.auth.signup.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation<RootDestination.Auth>(
        startDestination = AuthDestination.SignIn,
    ) {
        composable<AuthDestination.Onboarding> {
            OnboardingScreen(
                onboardingCompleted = {
                    navController.navigateTo(AuthDestination.RoleSelection)
                },
            )
        }
        composable<AuthDestination.RoleSelection> {
            RoleSelectionScreen(
                onNavigateToTouristGraph = {
                    navController.switchRoot(
                        targetDestination = RootDestination.Tourist,
                        clearBackStackFrom = RootDestination.Auth,
                    )
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
                onNavigateToRoleSelection = {
                    navController.navigateTo(AuthDestination.RoleSelection)
                },
            )
        }
        composable<AuthDestination.SignUp> {
            SignUpScreen(
                onNavigateToSignIn = { navController.navigateTo(AuthDestination.SignIn) },
            )
        }
        composable<AuthDestination.ForgotPassword> {
            ForgotPasswordScreen(
                onNavigateToSignIn = { navController.navigateTo(AuthDestination.SignIn) },
            )
        }
    }
}
