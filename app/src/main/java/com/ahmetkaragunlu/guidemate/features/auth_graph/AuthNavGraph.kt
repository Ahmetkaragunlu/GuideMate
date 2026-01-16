package com.ahmetkaragunlu.guidemate.features.auth_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ahmetkaragunlu.guidemate.features.graph.Graph
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.screens.auth.forgot_password.ForgotPasswordScreen
import com.ahmetkaragunlu.guidemate.screens.auth.onboarding.OnboardingScreen
import com.ahmetkaragunlu.guidemate.screens.auth.role_selection.RoleSelectionScreen
import com.ahmetkaragunlu.guidemate.screens.auth.sign_in.SignInScreen
import com.ahmetkaragunlu.guidemate.screens.auth.sign_up.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {

    navigation(
        startDestination = AuthRoute.SignInScreen.route,
        route = Graph.AuthGraph.route
    ) {
        composable(route = AuthRoute.OnboardingScreen.route) {
            OnboardingScreen(
                onboardingCompleted = {
                    navController.navigateTo(route = AuthRoute.RoleSelectionScreen.route)
                }
            )
        }
        composable(route = AuthRoute.RoleSelectionScreen.route) {
            RoleSelectionScreen()
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
                }
            )
        }
        composable(route = AuthRoute.SignUpScreen.route) {
            SignUpScreen(
                onNavigateToSignIn = { navController.navigateTo(AuthRoute.SignInScreen.route) }
            )
        }
        composable(route = AuthRoute.ForgotPassWordScreen.route) {
            ForgotPasswordScreen()
        }
    }
}