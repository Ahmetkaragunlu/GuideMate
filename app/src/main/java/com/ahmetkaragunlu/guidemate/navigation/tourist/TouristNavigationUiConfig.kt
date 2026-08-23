package com.ahmetkaragunlu.guidemate.navigation.tourist

import androidx.annotation.StringRes
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.components.AppTopBarConfig
import com.ahmetkaragunlu.guidemate.navigation.NavigationUiConfig
import com.ahmetkaragunlu.guidemate.navigation.chat.ChatDestination
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.TouristPaymentDestination

internal fun NavDestination?.touristNavigationUiConfig(): NavigationUiConfig =
    when {
        this == null || hasRoute<TouristDestination.Home>() ->
            touristNavigationUiConfig(
                titleResId = R.string.welcome_message,
                isHome = true,
            )
        hasRoute<TouristDestination.Explore>() ->
            touristNavigationUiConfig(titleResId = R.string.tourist_explore)
        hasRoute<TouristDestination.Trips>() ->
            touristNavigationUiConfig(titleResId = R.string.tourist_trips)
        hasRoute<TouristDestination.Chat>() ->
            touristNavigationUiConfig(titleResId = R.string.tourist_chat)
        hasRoute<TouristDestination.Profile>() ->
            touristNavigationUiConfig(
                titleResId = R.string.tourist_profile,
                showLogoutButton = true,
            )
        hasRoute<TouristDestination.Filter>() ->
            touristNavigationUiConfig(
                titleResId = R.string.filter,
                showBackButton = true,
                showBottomBar = false,
            )
        hasRoute<TouristDestination.TourDetail>() ->
            touristNavigationUiConfig(
                titleResId = R.string.tour_details,
                showBackButton = true,
                showBottomBar = false,
            )
        hasRoute<TouristDestination.ReservationDetail>() ->
            touristNavigationUiConfig(
                titleResId = R.string.tour_details,
                showBackButton = true,
                showBottomBar = false,
            )
        hasRoute<TouristDestination.GuideProfile>() ->
            touristNavigationUiConfig(
                titleResId = R.string.public_guide_profile,
                showBackButton = true,
                showBottomBar = false,
            )
        hasRoute<ChatDestination.Detail>() ->
            touristNavigationUiConfig(
                titleResId = R.string.tourist_chat,
                isChatDetail = true,
                showBackButton = true,
            )
        hasRoute<TouristPaymentDestination.Wallet>() ->
            touristNavigationUiConfig(
                titleResId = R.string.wallet,
                showBackButton = true,
                showBottomBar = false,
            )
        hasRoute<TouristPaymentDestination.WalletTransactions>() ->
            touristNavigationUiConfig(
                titleResId = R.string.wallet_transaction_history,
                showBackButton = true,
                showBottomBar = false,
            )
        hasRoute<TouristPaymentDestination.Checkout>() ->
            touristNavigationUiConfig(
                titleResId = R.string.checkout_topbar_title,
                showBackButton = true,
                showBottomBar = false,
            )
        hasRoute<TouristPaymentDestination.Hosted>() ->
            touristNavigationUiConfig(
                titleResId = R.string.secure_payment_title,
                showBottomBar = false,
            )
        hasRoute<TouristPaymentDestination.Status>() ||
            hasRoute<TouristPaymentDestination.Success>() ->
            touristNavigationUiConfig(
                titleResId = R.string.payment_topbar_title,
                showBottomBar = false,
            )
        else -> touristNavigationUiConfig(titleResId = R.string.app_name)
    }

private fun touristNavigationUiConfig(
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
