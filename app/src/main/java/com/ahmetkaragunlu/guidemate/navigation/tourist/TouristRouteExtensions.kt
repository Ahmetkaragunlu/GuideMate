package com.ahmetkaragunlu.guidemate.navigation.tourist

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R

internal fun String?.isTouristChatDetailRoute(): Boolean =
    this == TOURIST_CHAT_DETAIL_ROUTE_PATTERN

internal fun String?.isTouristFilterRoute(): Boolean =
    this == TouristRoute.TouristFilterScreen.route

internal fun String?.isTouristHomeRoute(): Boolean =
    this == TouristRoute.TouristHomeScreen.route

internal fun String?.isTouristProfileRoute(): Boolean =
    this == TouristRoute.TouristProfileScreen.route

internal fun String?.shouldShowTouristBackButton(): Boolean =
    isTouristFilterRoute() || isTouristChatDetailRoute()

@StringRes
internal fun String?.touristScreenTitleResId(): Int =
    when (this) {
        TouristRoute.TouristHomeScreen.route -> R.string.welcome_message
        TouristRoute.TouristExploreScreen.route -> R.string.tourist_explore
        TouristRoute.TouristMyTripsScreen.route -> R.string.tourist_trips
        TouristRoute.TouristChatScreen.route -> R.string.tourist_chat
        TouristRoute.TouristProfileScreen.route -> R.string.tourist_profile
        TouristRoute.TouristFilterScreen.route -> R.string.filter
        else -> R.string.app_name
    }
