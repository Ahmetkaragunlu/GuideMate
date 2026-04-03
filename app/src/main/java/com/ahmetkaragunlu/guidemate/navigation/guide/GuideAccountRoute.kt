package com.ahmetkaragunlu.guidemate.navigation.guide

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R

enum class GuideAccountRoute(
    val route: String,
    @StringRes val titleResId: Int,
) {
    BankAccounts(
        route = "guide_account_bank_accounts",
        titleResId = R.string.menu_bank_accounts,
    ),
    AboutLanguages(
        route = "guide_account_about_languages",
        titleResId = R.string.menu_about_languages,
    ),
    AccountSettings(
        route = "guide_account_settings",
        titleResId = R.string.menu_account_settings,
    ),
    HelpFaq(
        route = "guide_account_help_faq",
        titleResId = R.string.menu_help_faq,
    ),
    ;

    companion object {
        fun fromRoute(route: String?): GuideAccountRoute? = entries.firstOrNull { it.route == route }
    }
}
