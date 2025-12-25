package com.ahmetkaragunlu.guidemate.features.auth_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ahmetkaragunlu.guidemate.features.routes.AuthRoute
import com.ahmetkaragunlu.guidemate.features.routes.Graph
import com.ahmetkaragunlu.guidemate.screens.auth.ForgotPasswordScreen
import com.ahmetkaragunlu.guidemate.screens.auth.OnboardingScreen
import com.ahmetkaragunlu.guidemate.screens.auth.RoleSelectionScreen
import com.ahmetkaragunlu.guidemate.screens.auth.SignInScreen
import com.ahmetkaragunlu.guidemate.screens.auth.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {

    navigation(
        startDestination = AuthRoute.OnboardingScreen.route,
        route = Graph.AuthGraph.route
    ) {
        composable(route = AuthRoute.OnboardingScreen.route) {
            OnboardingScreen()
        }
        composable(route = AuthRoute.RoleSelectionScreen.route) {
            RoleSelectionScreen()
        }
        composable(route = AuthRoute.SignInScreen.route) {
            SignInScreen()
        }
        composable(route = AuthRoute.SignUpScreen.route) {
            SignUpScreen()
        }
        composable(route = AuthRoute.ForgotPassWord.route) {
            ForgotPasswordScreen()
        }
    }
}