package com.ahmetkaragunlu.guidemate.navigation.guide.account

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.ChangePasswordScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.account.helpsupport.HelpSupportScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.account.legalagreements.LegalAgreementsScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.account.about.GuideAboutScreen
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.GuideBankAccountsScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.account.helpsupport.model.guideFaqEntries
import com.ahmetkaragunlu.guidemate.profile.presentation.account.legalagreements.model.guideLegalClauses
import com.ahmetkaragunlu.guidemate.notification.presentation.guide.settings.GuideNotificationSettingsScreen

internal fun NavGraphBuilder.guideAccountNavGraph(onAboutSaved: () -> Unit) {
    composable<GuideAccountDestination.BankAccounts> {
        GuideBankAccountsScreen()
    }
    composable<GuideAccountDestination.About> {
        GuideAboutScreen(onSaved = onAboutSaved)
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
