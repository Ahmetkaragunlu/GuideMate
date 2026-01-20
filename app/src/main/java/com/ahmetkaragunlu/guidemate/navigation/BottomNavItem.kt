package com.ahmetkaragunlu.guidemate.navigation


import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.features.tourist_graph.TouristRoute
import compose.icons.TablerIcons
import compose.icons.tablericons.Compass
import compose.icons.tablericons.Home
import compose.icons.tablericons.MessageCircle2
import compose.icons.tablericons.Route
import compose.icons.tablericons.User

data class BottomNavItem(
    @StringRes val label: Int,
    val icon: ImageVector,
    val screen: String
)


val touristNavItems = listOf(
    BottomNavItem(
        label =R.string.tourist_home,
        icon = TablerIcons.Home,
        screen = TouristRoute.TouristHomeScreen.route
    ),
    BottomNavItem(
        label = R.string.tourist_explore,
        icon =TablerIcons.Compass,
        screen = TouristRoute.TouristExploreScreen.route
    ),
    BottomNavItem(
        label = R.string.tourist_trips,
        icon =TablerIcons.Route,
        screen = TouristRoute.TouristMyTripsScreen.route
    ),
    BottomNavItem(
        label = R.string.tourist_chat,
        icon = TablerIcons.MessageCircle2,
        screen = TouristRoute.TouristChatScreen.route
    ),
    BottomNavItem(
        label = R.string.tourist_profile,
        icon = TablerIcons.User,
        screen = TouristRoute.TouristProfileScreen.route
    ),
)