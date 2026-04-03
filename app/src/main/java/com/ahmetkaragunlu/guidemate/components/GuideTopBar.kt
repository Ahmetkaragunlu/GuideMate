package com.ahmetkaragunlu.guidemate.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.guide.GuideRoute

@Composable
fun GuideTopBar(
    currentRoute: String,
    navController: NavController,
    userName: String?,
) {
    val isChatDetail = currentRoute == GuideRoute.GuideChatDetailScreen.route
    val config =
        AppTopBarConfig(
            isHome = currentRoute == GuideRoute.GuideHomeScreen.route,
            isChatDetail = isChatDetail,
            showBackButton = isChatDetail,
            showLogoutButton = currentRoute == GuideRoute.GuideProfileScreen.route,
            titleResId = getGuideScreenTitle(currentRoute),
            chatTitle = "Hans Müller",
        )

    AppTopBar(
        config = config,
        userName = userName,
        onBackClick = navController::navigateUp,
        onLogoutClick = { },
    )
}

fun getGuideScreenTitle(route: String?): Int =
    when (route) {
        GuideRoute.GuideHomeScreen.route -> R.string.welcome_message
        GuideRoute.GuideMyToursScreen.route -> R.string.guide_tours
        GuideRoute.GuideMyWalletScreen.route -> R.string.guide_wallet
        GuideRoute.GuideChatScreen.route -> R.string.guide_chat
        GuideRoute.GuideProfileScreen.route -> R.string.guide_profile
        else -> R.string.app_name
    }
