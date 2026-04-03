package com.ahmetkaragunlu.guidemate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahmetkaragunlu.guidemate.navigation.auth.authNavGraph
import com.ahmetkaragunlu.guidemate.navigation.graph.Graph
import com.ahmetkaragunlu.guidemate.navigation.guide.GuideAccountNavGraphScaffold
import com.ahmetkaragunlu.guidemate.navigation.guide.GuideAccountRoute
import com.ahmetkaragunlu.guidemate.navigation.guide.GuideNavGraphScaffold
import com.ahmetkaragunlu.guidemate.navigation.tourist.AccountRoute
import com.ahmetkaragunlu.guidemate.navigation.tourist.TouristAccountNavGraphScaffold
import com.ahmetkaragunlu.guidemate.navigation.tourist.TouristNavGraphScaffold

@Composable
fun GuideMateNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Graph.TouristGraph.route,
    ) {
        authNavGraph(navController = navController)

        composable(route = Graph.TouristGraph.route) {
            TouristNavGraphScaffold(routeNavController = navController)
        }

        composable(route = Graph.GuideGraph.route) {
            GuideNavGraphScaffold(routeNavController = navController)
        }

        composable(
            route = "${Graph.GuideAccountGraph.route}/{targetRoute}",
            arguments = listOf(navArgument("targetRoute") { type = NavType.StringType }),
        ) { backStackEntry ->
            val targetRoute =
                backStackEntry.arguments?.getString("targetRoute")
                    ?: GuideAccountRoute.BankAccounts.route
            GuideAccountNavGraphScaffold(
                routeNavController = navController,
                startDestination = targetRoute,
            )
        }

        composable(
            route = "${Graph.AccountGraph.route}/{targetRoute}",
            arguments = listOf(navArgument("targetRoute") { type = NavType.StringType }),
        ) { backStackEntry ->
            val targetRoute =
                backStackEntry.arguments?.getString("targetRoute")
                    ?: AccountRoute.SavedCards.route
            TouristAccountNavGraphScaffold(
                routeNavController = navController,
                startDestination = targetRoute,
            )
        }
    }
}
