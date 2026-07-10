package com.ahmetkaragunlu.guidemate.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ahmetkaragunlu.guidemate.navigation.tourist.isTouristChatDetailRoute
import com.ahmetkaragunlu.guidemate.navigation.tourist.isTouristHomeRoute
import com.ahmetkaragunlu.guidemate.navigation.tourist.isTouristProfileRoute
import com.ahmetkaragunlu.guidemate.navigation.tourist.shouldShowTouristBackButton
import com.ahmetkaragunlu.guidemate.navigation.tourist.touristScreenTitleResId

@Composable
fun TouristAppBar(
    currentRoute: String,
    navController: NavController,
    userName: String?,
) {
    val isChatDetail = currentRoute.isTouristChatDetailRoute()
    val config =
        AppTopBarConfig(
            isHome = currentRoute.isTouristHomeRoute(),
            isChatDetail = isChatDetail,
            showBackButton = currentRoute.shouldShowTouristBackButton(),
            showLogoutButton = currentRoute.isTouristProfileRoute(),
            titleResId = currentRoute.touristScreenTitleResId(),
            chatTitle = "Ahmet Rehber",
        )

    AppTopBar(
        config = config,
        userName = userName,
        onBackClick = navController::navigateUp,
        onLogoutClick = { },
    )
}
