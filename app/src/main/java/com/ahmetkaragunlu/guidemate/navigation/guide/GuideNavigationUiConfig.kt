package com.ahmetkaragunlu.guidemate.navigation.guide

import androidx.annotation.StringRes
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.components.AppTopBarConfig
import com.ahmetkaragunlu.guidemate.navigation.NavigationUiConfig
import com.ahmetkaragunlu.guidemate.navigation.chat.ChatDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.wallet.GuideWalletDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.tours.GuideTourDestination

internal fun NavDestination?.guideNavigationUiConfig(): NavigationUiConfig =
    when {
        this == null || hasRoute<GuideDestination.Home>() ->
            guideNavigationUiConfig(
                titleResId = R.string.welcome_message,
                isHome = true,
            )
        hasRoute<GuideDestination.Chat>() ->
            guideNavigationUiConfig(titleResId = R.string.guide_chat)
        hasRoute<GuideDestination.Profile>() ->
            guideNavigationUiConfig(
                titleResId = R.string.guide_profile,
                showLogoutButton = true,
            )
        hasRoute<GuideDestination.ProfilePreview>() ->
            guideNavigationUiConfig(
                titleResId = R.string.preview_screen_title,
                showBackButton = true,
            )
        hasRoute<ChatDestination.Detail>() ->
            guideNavigationUiConfig(
                titleResId = R.string.guide_chat,
                isChatDetail = true,
                showBackButton = true,
            )
        hasRoute<GuideTourDestination.MyTours>() ->
            guideNavigationUiConfig(titleResId = R.string.guide_tours)
        hasRoute<GuideTourDestination.Detail>() ->
            guideNavigationUiConfig(
                titleResId = R.string.tour_details,
                showBackButton = true,
                showBottomBar = false,
            )
        hasRoute<GuideTourDestination.Edit>() ->
            guideNavigationUiConfig(
                titleResId = R.string.edit_tour,
                showBackButton = true,
                showBottomBar = false,
            )
        hasRoute<GuideTourDestination.PublishStep1>() ->
            publishNavigationUiConfig(R.string.guide_tour_publish_topbar_step1_title)
        hasRoute<GuideTourDestination.PublishStep2>() ->
            publishNavigationUiConfig(R.string.guide_tour_publish_topbar_step2_title)
        hasRoute<GuideTourDestination.PublishStep3>() ->
            publishNavigationUiConfig(R.string.guide_tour_publish_topbar_step3_title)
        hasRoute<GuideTourDestination.PublishStep4>() ->
            publishNavigationUiConfig(R.string.guide_tour_publish_topbar_step4_title)
        hasRoute<GuideWalletDestination.Wallet>() ->
            guideNavigationUiConfig(titleResId = R.string.guide_wallet)
        hasRoute<GuideWalletDestination.Earnings>() ->
            guideNavigationUiConfig(
                titleResId = R.string.guide_earnings,
                showBackButton = true,
                showBottomBar = false,
            )
        hasRoute<GuideWalletDestination.WalletTransactions>() ->
            guideNavigationUiConfig(
                titleResId = R.string.wallet_transaction_history,
                showBackButton = true,
                showBottomBar = false,
            )
        else -> guideNavigationUiConfig(titleResId = R.string.app_name)
    }

private fun publishNavigationUiConfig(
    @StringRes titleResId: Int,
): NavigationUiConfig =
    guideNavigationUiConfig(
        titleResId = titleResId,
        showBackButton = true,
        showBottomBar = false,
    )

private fun guideNavigationUiConfig(
    @StringRes titleResId: Int,
    isHome: Boolean = false,
    isChatDetail: Boolean = false,
    showBackButton: Boolean = false,
    showLogoutButton: Boolean = false,
    showBottomBar: Boolean = true,
): NavigationUiConfig =
    NavigationUiConfig(
        topBar =
            AppTopBarConfig(
                isHome = isHome,
                isChatDetail = isChatDetail,
                showBackButton = showBackButton,
                showLogoutButton = showLogoutButton,
                titleResId = titleResId,
            ),
        showBottomBar = showBottomBar,
    )
