package com.ahmetkaragunlu.guidemate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ahmetkaragunlu.guidemate.features.auth_graph.authNavGraph
import com.ahmetkaragunlu.guidemate.features.routes.Graph

@Composable
fun GuideMateNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Graph.AuthGraph.route) {
      authNavGraph(navController = navController)
    }
}