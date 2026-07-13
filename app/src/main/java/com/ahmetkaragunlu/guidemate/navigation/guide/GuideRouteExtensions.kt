package com.ahmetkaragunlu.guidemate.navigation.guide

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R

private val guideTourPublishRoutes =
    setOf(
        GuideRoute.GuideTourPublishStep1Screen.route,
        GuideRoute.GuideTourPublishStep2Screen.route,
        GuideRoute.GuideTourPublishStep3Screen.route,
        GuideRoute.GuideTourPublishStep4Screen.route,
    )

internal fun String?.isGuideTourPublishRoute(): Boolean = this in guideTourPublishRoutes

internal fun String?.isGuideChatDetailRoute(): Boolean =
    this == GuideRoute.GuideChatDetailScreen.route

internal fun String?.isGuideProfilePreviewRoute(): Boolean =
    this == GuideRoute.GuideProfilePreviewScreen.route

internal fun String?.isGuideEarningsRoute(): Boolean =
    this == GuideRoute.GuideEarningsScreen.route

internal fun String?.isGuideTourDetailRoute(): Boolean =
    this == GUIDE_TOUR_DETAIL_ROUTE_PATTERN

internal fun String?.isGuideTourEditRoute(): Boolean =
    this == GUIDE_TOUR_EDIT_ROUTE_PATTERN

internal fun String?.isGuideHomeRoute(): Boolean =
    this == GuideRoute.GuideHomeScreen.route

internal fun String?.isGuideProfileRoute(): Boolean =
    this == GuideRoute.GuideProfileScreen.route

internal fun String?.shouldShowGuideBackButton(): Boolean =
    isGuideChatDetailRoute() ||
        isGuideProfilePreviewRoute() ||
        isGuideEarningsRoute() ||
        isGuideTourDetailRoute() ||
        isGuideTourEditRoute() ||
        isGuideTourPublishRoute()

internal fun String?.shouldShowGuideBottomBar(): Boolean =
    !isGuideTourPublishRoute() &&
        !isGuideEarningsRoute() &&
        !isGuideTourDetailRoute() &&
        !isGuideTourEditRoute()

@StringRes
internal fun String?.guideScreenTitleResId(): Int =
    when (this) {
        GuideRoute.GuideHomeScreen.route -> R.string.welcome_message
        GuideRoute.GuideEarningsScreen.route -> R.string.guide_earnings
        GuideRoute.GuideMyToursScreen.route -> R.string.guide_tours
        GUIDE_TOUR_DETAIL_ROUTE_PATTERN -> R.string.tour_details
        GUIDE_TOUR_EDIT_ROUTE_PATTERN -> R.string.edit_tour
        GuideRoute.GuideMyWalletScreen.route -> R.string.guide_wallet
        GuideRoute.GuideChatScreen.route -> R.string.guide_chat
        GuideRoute.GuideProfileScreen.route -> R.string.guide_profile
        GuideRoute.GuideProfilePreviewScreen.route -> R.string.preview_screen_title
        GuideRoute.GuideTourPublishStep1Screen.route -> R.string.guide_tour_publish_topbar_step1_title
        GuideRoute.GuideTourPublishStep2Screen.route -> R.string.guide_tour_publish_topbar_step2_title
        GuideRoute.GuideTourPublishStep3Screen.route -> R.string.guide_tour_publish_topbar_step3_title
        GuideRoute.GuideTourPublishStep4Screen.route -> R.string.guide_tour_publish_topbar_step4_title
        else -> R.string.app_name
    }
