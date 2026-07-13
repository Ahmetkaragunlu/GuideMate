package com.ahmetkaragunlu.guidemate.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.guide.guideScreenTitleResId
import com.ahmetkaragunlu.guidemate.navigation.guide.isGuideChatDetailRoute
import com.ahmetkaragunlu.guidemate.navigation.guide.isGuideHomeRoute
import com.ahmetkaragunlu.guidemate.navigation.guide.isGuideProfileRoute
import com.ahmetkaragunlu.guidemate.navigation.guide.shouldShowGuideBackButton

@Composable
fun GuideTopBar(
    currentRoute: String,
    navController: NavController,
    userName: String?,
    unreadNotificationCount: Int,
    onNotificationClick: () -> Unit,
    chatTitle: String = "",
    chatAvatarResId: Int = R.drawable.example,
    onBackClick: () -> Unit = navController::navigateUp,
) {
    val isChatDetail = currentRoute.isGuideChatDetailRoute()
    val config =
        AppTopBarConfig(
            isHome = currentRoute.isGuideHomeRoute(),
            isChatDetail = isChatDetail,
            showBackButton = currentRoute.shouldShowGuideBackButton(),
            showLogoutButton = currentRoute.isGuideProfileRoute(),
            titleResId = currentRoute.guideScreenTitleResId(),
            chatTitle = chatTitle,
            chatAvatarResId = chatAvatarResId,
        )

    AppTopBar(
        config = config,
        userName = userName,
        onBackClick = onBackClick,
        onLogoutClick = { },
        unreadNotificationCount = unreadNotificationCount,
        onNotificationClick = onNotificationClick,
    )
}
