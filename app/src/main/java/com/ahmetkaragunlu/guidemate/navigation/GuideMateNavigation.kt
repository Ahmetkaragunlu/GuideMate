package com.ahmetkaragunlu.guidemate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.navigation.auth.authNavGraph
import com.ahmetkaragunlu.guidemate.navigation.guide.GuideNavigation
import com.ahmetkaragunlu.guidemate.navigation.guide.account.GuideAccountNavigation
import com.ahmetkaragunlu.guidemate.navigation.tourist.TouristNavigation
import com.ahmetkaragunlu.guidemate.navigation.tourist.account.TouristAccountNavigation

@Composable
fun GuideMateNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RootDestination.Guide,
    ) {
        authNavGraph(navController = navController)

        composable<RootDestination.Tourist> {
            TouristNavigation(routeNavController = navController)
        }

        composable<RootDestination.Guide> {
            GuideNavigation(routeNavController = navController)
        }

        composable<RootDestination.GuideAccount> { backStackEntry ->
            val destination = backStackEntry.toRoute<RootDestination.GuideAccount>()
            GuideAccountNavigation(
                startDestination = destination.startDestination,
                onClose = navController::navigateUp,
            )
        }

        composable<RootDestination.TouristAccount> { backStackEntry ->
            val destination = backStackEntry.toRoute<RootDestination.TouristAccount>()
            TouristAccountNavigation(
                startDestination = destination.startDestination,
                onClose = navController::navigateUp,
            )
        }
    }
}
