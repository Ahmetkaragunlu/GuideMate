package com.ahmetkaragunlu.guidemate.navigation.guide

enum class GuideRoute(
    val route: String,
) {
    GuideHomeScreen(route = "GuideHomeScreen"),
    GuideEarningsScreen(route = "GuideEarningsScreen"),
    GuideMyToursScreen(route = "GuideMyToursScreen"),
    GuideTourDetailScreen(route = "GuideTourDetailScreen"),
    GuideTourEditScreen(route = "GuideTourEditScreen"),
    GuideTourPublishStep1Screen(route = "GuideTourPublishStep1Screen"),
    GuideTourPublishStep2Screen(route = "GuideTourPublishStep2Screen"),
    GuideTourPublishStep3Screen(route = "GuideTourPublishStep3Screen"),
    GuideTourPublishStep4Screen(route = "GuideTourPublishStep4Screen"),
    GuideProfileScreen(route = "GuideProfileScreen"),
    GuideProfilePreviewScreen(route = "GuideProfilePreviewScreen"),
    GuideChatScreen(route = "GuideChatScreen"),
    GuideMyWalletScreen(route = "GuideMyWalletScreen"),
    GuideChatDetailScreen(route = "GuideChatDetailScreen"),
}

internal const val GUIDE_TOUR_SESSION_ID_ARGUMENT = "sessionId"

internal val GUIDE_TOUR_DETAIL_ROUTE_PATTERN =
    "${GuideRoute.GuideTourDetailScreen.route}/{$GUIDE_TOUR_SESSION_ID_ARGUMENT}"

internal fun guideTourDetailRoute(sessionId: String): String =
    "${GuideRoute.GuideTourDetailScreen.route}/$sessionId"

internal val GUIDE_TOUR_EDIT_ROUTE_PATTERN =
    "${GuideRoute.GuideTourEditScreen.route}/{$GUIDE_TOUR_SESSION_ID_ARGUMENT}"

internal fun guideTourEditRoute(sessionId: String): String =
    "${GuideRoute.GuideTourEditScreen.route}/$sessionId"
