package com.ahmetkaragunlu.guidemate.navigation.guide

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R

enum class GuideAccountRoute(
    val route: String,
    @StringRes val titleResId: Int,
) {
    SavedCards(
        route = "guide_account_saved_cards",
        titleResId = R.string.saved_cards,
    ),
    About(
        route = "guide_account_about",
        titleResId = R.string.about,
    ),
    ChangePassword(
        route = "guide_account_change_password",
        titleResId = R.string.change_password,
    ),
    NotificationSettings(
        route = "guide_account_notification_settings",
        titleResId = R.string.notification_settings,
    ),
    LegalAgreements(
        route = "guide_account_legal_agreements",
        titleResId = R.string.legal_agreements,
    ),
    HelpSupport(
        route = "guide_account_help_support",
        titleResId = R.string.help_support,
    ),
    ;

    companion object {
        fun fromRoute(route: String?): GuideAccountRoute? = entries.firstOrNull { it.route == route }
    }
}
