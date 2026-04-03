package com.ahmetkaragunlu.guidemate.navigation.tourist

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R

enum class AccountRoute(
    val route: String,
    @StringRes val titleResId: Int,
) {
    SavedCards(
        route = "account_saved_cards",
        titleResId = R.string.saved_cards,
    ),
    ChangePassword(
        route = "account_change_password",
        titleResId = R.string.change_password,
    ),
    NotificationSettings(
        route = "account_notification_settings",
        titleResId = R.string.notification_settings,
    ),
    LegalAgreements(
        route = "account_legal_agreements",
        titleResId = R.string.legal_agreements,
    ),
    HelpSupport(
        route = "account_help_support",
        titleResId = R.string.help_support,
    ),
    ;

    companion object {
        fun fromRoute(route: String?): AccountRoute? = entries.firstOrNull { it.route == route }
    }
}
