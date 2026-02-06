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
    val type: ProfileMenuType,
    @StringRes val titleResId: Int,
    val icon: ImageVector
)



 val menuOptions = listOf(
    ProfileMenuOption(ProfileMenuType.CARDS, R.string.menu_cards, TablerIcons.CreditCard),
    ProfileMenuOption(ProfileMenuType.PASSWORD, R.string.menu_password, TablerIcons.Lock),
    ProfileMenuOption(ProfileMenuType.NOTIFICATIONS, R.string.menu_notifications, TablerIcons.Bell),
    ProfileMenuOption(ProfileMenuType.LEGAL, R.string.menu_legal, TablerIcons.Scale),
    ProfileMenuOption(ProfileMenuType.HELP, R.string.menu_help, TablerIcons.Help)
)