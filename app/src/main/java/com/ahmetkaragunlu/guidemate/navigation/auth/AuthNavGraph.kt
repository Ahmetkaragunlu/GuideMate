package com.ahmetkaragunlu.guidemate.navigation.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ahmetkaragunlu.guidemate.navigation.graph.Graph
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.navigation.switchRoot
import com.ahmetkaragunlu.guidemate.screens.auth.forgotpassword.ForgotPasswordScreen
import com.ahmetkaragunlu.guidemate.screens.auth.onboarding.OnboardingScreen
import com.ahmetkaragunlu.guidemate.screens.auth.roleselection.RoleSelectionScreen
import com.ahmetkaragunlu.guidemate.screens.auth.signin.SignInScreen
import com.ahmetkaragunlu.guidemate.screens.auth.signup.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        startDestination = AuthRoute.SignInScreen.route,
        route = Graph.AuthGraph.route,
    ) {
        composable(route = AuthRoute.OnboardingScreen.route) {
            OnboardingScreen(
                onboardingCompleted = {
                    navController.navigateTo(route = AuthRoute.RoleSelectionScreen.route)
                },
            )
        }
        composable(route = AuthRoute.RoleSelectionScreen.route) {
            RoleSelectionScreen(
                onNavigateToTouristGraph = {
                    navController.switchRoot(
                        targetRoute = Graph.TouristGraph.route,
                        clearBackStackFrom = Graph.AuthGraph.route,
                    )
                },
            )
        }
        composable(route = AuthRoute.SignInScreen.route) {
            SignInScreen(
                onNavigateToSignUp = {
                    navController.navigateTo(AuthRoute.SignUpScreen.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigateTo(AuthRoute.ForgotPassWordScreen.route)
                },
                onNavigateToRoleSelection = {
                    navController.navigateTo(AuthRoute.RoleSelectionScreen.route)
                },
            )
        }
        composable(route = AuthRoute.SignUpScreen.route) {
            SignUpScreen(
                onNavigateToSignIn = { navController.navigateTo(AuthRoute.SignInScreen.route) },
            )
        }
        composable(route = AuthRoute.ForgotPassWordScreen.route) {
            ForgotPasswordScreen(
                onNavigateToSignIn = { navController.navigateTo(AuthRoute.SignInScreen.route) },
            )
        }
    }
}
