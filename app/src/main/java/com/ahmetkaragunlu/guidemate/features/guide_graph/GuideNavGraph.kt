package com.ahmetkaragunlu.guidemate.features.guide_graph

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
import com.ahmetkaragunlu.guidemate.components.GuideTopBar
import com.ahmetkaragunlu.guidemate.navigation.guideNavItems
import com.ahmetkaragunlu.guidemate.screens.guide.GuideChatScreen

import com.ahmetkaragunlu.guidemate.screens.guide.GuideMyToursScreen
import com.ahmetkaragunlu.guidemate.screens.guide.GuideMyWalletScreen
import com.ahmetkaragunlu.guidemate.screens.guide.GuideProfileScreen
import com.ahmetkaragunlu.guidemate.screens.guide.home.GuideHomeScreen
import com.ahmetkaragunlu.guidemate.screens.guide.home.GuideHomeViewModel


fun NavGraphBuilder.guideNavGraph(
   guideNavController: NavController,
   routeNavController: NavController
) {
    composable(route = GuideRoute.GuideHomeScreen.route) {
        GuideHomeScreen()
    }
    composable(route = GuideRoute.GuideMyToursScreen.route) {
        GuideMyToursScreen()
    }
    composable(route = GuideRoute.GuideMyWalletScreen.route) {
        GuideMyWalletScreen()
    }
    composable(route = GuideRoute.GuideChatScreen.route) {
        GuideChatScreen()
    }
    composable(route = GuideRoute.GuideProfileScreen.route) {
        GuideProfileScreen()
    }
}






@Composable
fun GuideNavGraphScaffold(
    routeNavController: NavController,
    viewModel: GuideHomeViewModel = hiltViewModel()
) {
    val guideNavController = rememberNavController()
    val navBackStackEntry by guideNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: GuideRoute.GuideHomeScreen.route
    val getUserName by viewModel.userName.collectAsStateWithLifecycle()



    Scaffold(
        topBar = {
            GuideTopBar(
                currentRoute = currentRoute,
                userName = getUserName
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = guideNavController,
                currentRoute = currentRoute,
                items = guideNavItems
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = guideNavController,
            startDestination = GuideRoute.GuideHomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            guideNavGraph(
                guideNavController = guideNavController,
                routeNavController = routeNavController
            )
        }
    }
}