package com.ahmetkaragunlu.guidemate.screens.guide.profile.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.R
import compose.icons.TablerIcons
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Help
import compose.icons.tablericons.Settings

data class ProfileMenuAction(
    val icon: ImageVector,
    @StringRes val titleResId: Int,
    val onClick: () -> Unit
)

val guideProfileMenuItems = listOf(
    ProfileMenuAction(TablerIcons.CreditCard, R.string.menu_bank_accounts) { /* TODO */ },
    ProfileMenuAction(Icons.Rounded.EditNote, R.string.menu_about_languages) { /* TODO */ },
    ProfileMenuAction(TablerIcons.Settings, R.string.menu_account_settings) { /* TODO */ },
    ProfileMenuAction(TablerIcons.Help, R.string.menu_help_faq) { /* TODO */ }
)