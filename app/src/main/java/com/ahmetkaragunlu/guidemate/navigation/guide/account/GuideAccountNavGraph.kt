package com.ahmetkaragunlu.guidemate.navigation.guide.account

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.ChangePasswordScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.account.helpsupport.HelpSupportScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.account.legalagreements.LegalAgreementsScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.account.about.AboutScreen
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.BankAccountsScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.account.helpsupport.model.guideFaqEntries
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.account.legalagreements.model.guideLegalClauses
import com.ahmetkaragunlu.guidemate.notification.presentation.guide.settings.GuideNotificationSettingsScreen

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
        GuideNotificationSettingsScreen()
    }
    composable<GuideAccountDestination.LegalAgreements> {
        LegalAgreementsScreen(
            titleResId = R.string.guide_legal_title,
            introResId = R.string.guide_legal_intro,
            legalClauses = guideLegalClauses,
        )
    }
    composable<GuideAccountDestination.HelpSupport> {
        HelpSupportScreen(
            introResId = R.string.guide_support_intro,
            faqEntries = guideFaqEntries,
        )
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
