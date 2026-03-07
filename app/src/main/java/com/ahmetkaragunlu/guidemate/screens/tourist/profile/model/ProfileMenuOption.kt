package com.ahmetkaragunlu.guidemate.screens.tourist.profile.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.features.tourist_graph.AccountRoute
import compose.icons.TablerIcons
import compose.icons.tablericons.Bell
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Help
import compose.icons.tablericons.Lock
import compose.icons.tablericons.Scale

data class ProfileMenuOption(
    val icon: ImageVector,
    @StringRes val titleResId: Int,
    val targetRoute: String
)


val menuOptions = listOf(
    ProfileMenuOption(
        icon = TablerIcons.CreditCard,
        titleResId = R.string.saved_cards,
        targetRoute = AccountRoute.SavedCards.route
    ),
    ProfileMenuOption(
        icon = TablerIcons.Lock,
        titleResId = R.string.change_password,
        targetRoute = AccountRoute.ChangePassword.route
    ),
    ProfileMenuOption(
        icon = TablerIcons.Bell,
        titleResId = R.string.notification_settings,
        targetRoute = AccountRoute.NotificationSettings.route
    ),
    ProfileMenuOption(
        icon = TablerIcons.Scale,
        titleResId = R.string.legal_agreements,
        targetRoute = AccountRoute.LegalAgreements.route
    ),
    ProfileMenuOption(
        icon = TablerIcons.Help,
        titleResId = R.string.help_support,
        targetRoute = AccountRoute.HelpSupport.route
    )
)