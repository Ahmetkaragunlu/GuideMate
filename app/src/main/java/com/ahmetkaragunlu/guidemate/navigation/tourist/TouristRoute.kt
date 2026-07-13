package com.ahmetkaragunlu.guidemate.navigation.tourist

import com.ahmetkaragunlu.guidemate.navigation.chat.chatDetailRoute
import com.ahmetkaragunlu.guidemate.navigation.chat.chatDetailRoutePattern

enum class TouristRoute(
    val route: String,
) {
    TouristHomeScreen(route = "TouristHomeScreen"),
    TouristProfileScreen(route = "TouristProfileScreen"),
    TouristChatScreen(route = "TouristChatScreen"),
    TouristExploreScreen(route = "TouristExploreScreen"),
    TouristMyTripsScreen(route = "TouristMyTripsScreen"),
    TouristFilterScreen(route = "TouristFilterScreen"),
    TouristChatDetailScreen(route = "TouristChatDetailScreen"),
}

internal val TOURIST_CHAT_DETAIL_ROUTE_PATTERN =
    chatDetailRoutePattern(TouristRoute.TouristChatDetailScreen.route)

internal fun touristChatDetailRoute(chatId: String): String =
    chatDetailRoute(TouristRoute.TouristChatDetailScreen.route, chatId)
