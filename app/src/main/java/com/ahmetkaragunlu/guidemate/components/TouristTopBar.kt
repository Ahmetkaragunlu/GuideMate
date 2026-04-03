package com.ahmetkaragunlu.guidemate.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.tourist.TouristRoute

@Composable
fun TouristAppBar(
    currentRoute: String,
    navController: NavController,
    userName: String?,
) {
    val isChatDetail = currentRoute == TouristRoute.TouristChatDetailScreen.route
    val config =
        AppTopBarConfig(
            isHome = currentRoute == TouristRoute.TouristHomeScreen.route,
            isChatDetail = isChatDetail,
            showBackButton = currentRoute == TouristRoute.TouristFilterScreen.route || isChatDetail,
            showLogoutButton = currentRoute == TouristRoute.TouristProfileScreen.route,
            titleResId = getTouristScreenTitle(currentRoute),
            chatTitle = "Ahmet Rehber",
        )

    AppTopBar(
        config = config,
        userName = userName,
        onBackClick = navController::navigateUp,
        onLogoutClick = { },
    )
}

fun getTouristScreenTitle(route: String?): Int =
    when (route) {
        TouristRoute.TouristHomeScreen.route -> R.string.welcome_message
        TouristRoute.TouristExploreScreen.route -> R.string.tourist_explore
        TouristRoute.TouristMyTripsScreen.route -> R.string.tourist_trips
        TouristRoute.TouristChatScreen.route -> R.string.tourist_chat
        TouristRoute.TouristProfileScreen.route -> R.string.tourist_profile
        TouristRoute.TouristFilterScreen.route -> R.string.filter
        else -> R.string.app_name
    }
