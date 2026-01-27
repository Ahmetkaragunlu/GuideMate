package com.ahmetkaragunlu.guidemate.features.tourist_graph

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ahmetkaragunlu.guidemate.components.AppBottomBar
import com.ahmetkaragunlu.guidemate.components.TouristAppBar
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.navigation.touristNavItems
import com.ahmetkaragunlu.guidemate.screens.tourist.TouristChatScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.TouristTripsScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.TouristProfileScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.explore.TouristExploreScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.explore.TouristFilterScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.home.TouristHomeScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.home.TouristHomeViewModel


fun NavGraphBuilder.touristNavGraph(
    touristNavController: NavController,
    routeNavController: NavController
) {
    composable(route = TouristRoute.TouristHomeScreen.route) {
        TouristHomeScreen()
    }
    composable(route = TouristRoute.TouristExploreScreen.route) {
        TouristExploreScreen(
            onNavigateToFilter = {
                touristNavController.navigateTo(TouristRoute.TouristFilterScreen.route)
            }
        )
    }
    composable(route = TouristRoute.TouristMyTripsScreen.route) {
        TouristTripsScreen()
    }
    composable(route = TouristRoute.TouristChatScreen.route) {
        TouristChatScreen()
    }

    composable(route = TouristRoute.TouristProfileScreen.route) {
        TouristProfileScreen()
    }
    composable(route = TouristRoute.TouristFilterScreen.route) {
        TouristFilterScreen()
    }
}



@Composable
fun TouristNavGraphScaffold(
    viewModel: TouristHomeViewModel = hiltViewModel(),
    routeNavController: NavController
) {
    val touristNavController = rememberNavController()
    val navBackStackEntry by touristNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: TouristRoute.TouristHomeScreen.route
    val getUserName by viewModel.userName.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TouristAppBar(
                currentRoute = currentRoute,
                navController = touristNavController,
                userName = getUserName
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = touristNavController,
                currentRoute = currentRoute,
                items = touristNavItems
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = touristNavController,
            startDestination = TouristRoute.TouristHomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            touristNavGraph(
                touristNavController = touristNavController,
                routeNavController = routeNavController
            )
        }
    }
}




