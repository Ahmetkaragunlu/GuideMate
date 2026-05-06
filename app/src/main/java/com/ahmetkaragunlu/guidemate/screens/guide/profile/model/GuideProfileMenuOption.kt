package com.ahmetkaragunlu.guidemate.screens.guide.profile.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.guide.GuideAccountRoute
import compose.icons.TablerIcons
import compose.icons.tablericons.Bell
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Help
import compose.icons.tablericons.Lock
import compose.icons.tablericons.Scale
import compose.icons.tablericons.User

data class GuideProfileMenuOption(
    val icon: ImageVector,
    @StringRes val titleResId: Int,
    val targetRoute: GuideAccountRoute,
)

val guideProfileMenuOptions =
    listOf(
        GuideProfileMenuOption(
            icon = TablerIcons.CreditCard,
            titleResId = R.string.saved_cards,
            targetRoute = GuideAccountRoute.SavedCards,
        ),
        GuideProfileMenuOption(
            icon = TablerIcons.User,
            titleResId = R.string.about,
            targetRoute = GuideAccountRoute.About,
        ),
        GuideProfileMenuOption(
            icon = TablerIcons.Lock,
            titleResId = R.string.change_password,
            targetRoute = GuideAccountRoute.ChangePassword,
        ),
        GuideProfileMenuOption(
            icon = TablerIcons.Bell,
            titleResId = R.string.notification_settings,
            targetRoute = GuideAccountRoute.NotificationSettings,
        ),
        GuideProfileMenuOption(
            icon = TablerIcons.Scale,
            titleResId = R.string.legal_agreements,
            targetRoute = GuideAccountRoute.LegalAgreements,
        ),
        GuideProfileMenuOption(
            icon = TablerIcons.Help,
            titleResId = R.string.help_support,
            targetRoute = GuideAccountRoute.HelpSupport,
        ),
    )
