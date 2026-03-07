package com.ahmetkaragunlu.guidemate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahmetkaragunlu.guidemate.features.auth_graph.authNavGraph
import com.ahmetkaragunlu.guidemate.features.graph.Graph
import com.ahmetkaragunlu.guidemate.features.guide_graph.GuideNavGraphScaffold
import com.ahmetkaragunlu.guidemate.features.tourist_graph.AccountNavGraphScaffold
import com.ahmetkaragunlu.guidemate.features.tourist_graph.AccountRoute
import com.ahmetkaragunlu.guidemate.features.tourist_graph.TouristNavGraphScaffold

@Composable
fun GuideMateNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Graph.TouristGraph.route
    ) {
        authNavGraph(navController = navController)

        composable(route = Graph.TouristGraph.route) {
            TouristNavGraphScaffold(routeNavController = navController)
        }

        composable(route = Graph.GuideGraph.route) {
            GuideNavGraphScaffold(routeNavController = navController)
        }

        composable(
            route = "${Graph.AccountGraph.route}/{targetRoute}",
            arguments = listOf(navArgument("targetRoute") { type = NavType.StringType })
        ) { backStackEntry ->
            val targetRoute = backStackEntry.arguments?.getString("targetRoute")
                ?: AccountRoute.SavedCards.route
            AccountNavGraphScaffold(
                routeNavController = navController,
                startDestination = targetRoute
            )
        }
    }
}