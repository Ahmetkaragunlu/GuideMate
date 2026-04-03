package com.ahmetkaragunlu.guidemate.screens.guide.profile.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.guide.GuideAccountRoute
import compose.icons.TablerIcons
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Help
import compose.icons.tablericons.Settings

data class GuideProfileMenuOption(
    val icon: ImageVector,
    @StringRes val titleResId: Int,
    val targetRoute: GuideAccountRoute,
)

val guideProfileMenuOptions =
    listOf(
        GuideProfileMenuOption(
            icon = TablerIcons.CreditCard,
            titleResId = R.string.menu_bank_accounts,
            targetRoute = GuideAccountRoute.BankAccounts,
        ),
        GuideProfileMenuOption(
            icon = Icons.Rounded.EditNote,
            titleResId = R.string.menu_about_languages,
            targetRoute = GuideAccountRoute.AboutLanguages,
        ),
        GuideProfileMenuOption(
            icon = TablerIcons.Settings,
            titleResId = R.string.menu_account_settings,
            targetRoute = GuideAccountRoute.AccountSettings,
        ),
        GuideProfileMenuOption(
            icon = TablerIcons.Help,
            titleResId = R.string.menu_help_faq,
            targetRoute = GuideAccountRoute.HelpFaq,
        ),
    )
