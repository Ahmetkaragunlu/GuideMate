package com.ahmetkaragunlu.guidemate.navigation.guide.account

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.changepassword.ChangePasswordScreen
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.AboutScreen
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.bankaccounts.BankAccountsScreen
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.helpsupport.HelpSupportScreen
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.legalagreements.LegalAgreementsScreen
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.notificationsettings.NotificationSettingsScreen

internal fun NavGraphBuilder.guideAccountNavGraph(onAboutSaved: () -> Unit) {
    composable<GuideAccountDestination.BankAccounts> {
        BankAccountsScreen()
    }
    composable<GuideAccountDestination.About> {
        AboutScreen(onSaved = onAboutSaved)
    }
    composable<GuideAccountDestination.ChangePassword> {
        ChangePasswordScreen()
    }
    composable<GuideAccountDestination.NotificationSettings> {
        NotificationSettingsScreen()
    }
    composable<GuideAccountDestination.LegalAgreements> {
        LegalAgreementsScreen()
    }
    composable<GuideAccountDestination.HelpSupport> {
        HelpSupportScreen()
    }
}

internal fun NavDestination?.guideAccountTitleResId(): Int =
    when {
        this == null || hasRoute<GuideAccountDestination.BankAccounts>() -> R.string.bank_accounts
        hasRoute<GuideAccountDestination.About>() -> R.string.about
        hasRoute<GuideAccountDestination.ChangePassword>() -> R.string.change_password
        hasRoute<GuideAccountDestination.NotificationSettings>() -> R.string.notification_settings
        hasRoute<GuideAccountDestination.LegalAgreements>() -> R.string.legal_agreements
        hasRoute<GuideAccountDestination.HelpSupport>() -> R.string.help_support
        else -> R.string.bank_accounts
    }
