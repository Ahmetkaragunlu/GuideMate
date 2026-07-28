package com.ahmetkaragunlu.guidemate.screens.guide.profile.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.R
import compose.icons.TablerIcons
import compose.icons.tablericons.Bell
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Help
import compose.icons.tablericons.Lock
import compose.icons.tablericons.Scale
import compose.icons.tablericons.User

data class GuideProfileMenuOption(
    val icon: ImageVector,
    @param:StringRes val titleResId: Int,
    val target: GuideProfileMenuTarget,
)

enum class GuideProfileMenuTarget {
    BANK_ACCOUNTS,
    ABOUT,
    CHANGE_PASSWORD,
    NOTIFICATION_SETTINGS,
    LEGAL_AGREEMENTS,
    HELP_SUPPORT,
}

val guideProfileMenuOptions =
    listOf(
        GuideProfileMenuOption(
            icon = TablerIcons.CreditCard,
            titleResId = R.string.bank_accounts,
            target = GuideProfileMenuTarget.BANK_ACCOUNTS,
        ),
        GuideProfileMenuOption(
            icon = TablerIcons.User,
            titleResId = R.string.about,
            target = GuideProfileMenuTarget.ABOUT,
        ),
        GuideProfileMenuOption(
            icon = TablerIcons.Lock,
            titleResId = R.string.change_password,
            target = GuideProfileMenuTarget.CHANGE_PASSWORD,
        ),
        GuideProfileMenuOption(
            icon = TablerIcons.Bell,
            titleResId = R.string.notification_settings,
            target = GuideProfileMenuTarget.NOTIFICATION_SETTINGS,
        ),
        GuideProfileMenuOption(
            icon = TablerIcons.Scale,
            titleResId = R.string.legal_agreements,
            target = GuideProfileMenuTarget.LEGAL_AGREEMENTS,
        ),
        GuideProfileMenuOption(
            icon = TablerIcons.Help,
            titleResId = R.string.help_support,
            target = GuideProfileMenuTarget.HELP_SUPPORT,
        ),
    )
