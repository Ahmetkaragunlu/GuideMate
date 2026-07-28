package com.ahmetkaragunlu.guidemate.screens.tourist.profile.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.R
import compose.icons.TablerIcons
import compose.icons.tablericons.Bell
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Help
import compose.icons.tablericons.Lock
import compose.icons.tablericons.Scale

data class ProfileMenuOption(
    val icon: ImageVector,
    @param:StringRes val titleResId: Int,
    val target: TouristProfileMenuTarget,
)

enum class TouristProfileMenuTarget {
    SAVED_CARDS,
    CHANGE_PASSWORD,
    NOTIFICATION_SETTINGS,
    LEGAL_AGREEMENTS,
    HELP_SUPPORT,
}

val menuOptions =
    listOf(
        ProfileMenuOption(
            icon = TablerIcons.CreditCard,
            titleResId = R.string.saved_cards,
            target = TouristProfileMenuTarget.SAVED_CARDS,
        ),
        ProfileMenuOption(
            icon = TablerIcons.Lock,
            titleResId = R.string.change_password,
            target = TouristProfileMenuTarget.CHANGE_PASSWORD,
        ),
        ProfileMenuOption(
            icon = TablerIcons.Bell,
            titleResId = R.string.notification_settings,
            target = TouristProfileMenuTarget.NOTIFICATION_SETTINGS,
        ),
        ProfileMenuOption(
            icon = TablerIcons.Scale,
            titleResId = R.string.legal_agreements,
            target = TouristProfileMenuTarget.LEGAL_AGREEMENTS,
        ),
        ProfileMenuOption(
            icon = TablerIcons.Help,
            titleResId = R.string.help_support,
            target = TouristProfileMenuTarget.HELP_SUPPORT,
        ),
    )
