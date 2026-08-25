package com.ahmetkaragunlu.guidemate.navigation.tourist.account

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.ChangePasswordScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.account.helpsupport.HelpSupportScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.account.legalagreements.LegalAgreementsScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.account.helpsupport.model.touristFaqEntries
import com.ahmetkaragunlu.guidemate.profile.presentation.account.legalagreements.model.touristLegalClauses
import com.ahmetkaragunlu.guidemate.notification.presentation.tourist.settings.TouristNotificationSettingsScreen
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.TouristSavedCardsScreen

internal fun NavGraphBuilder.touristAccountNavGraph(accountNavController: NavController) {
    composable<TouristAccountDestination.SavedCards> { TouristSavedCardsScreen() }
    composable<TouristAccountDestination.ChangePassword> {
        ChangePasswordScreen()
    }
    composable<TouristAccountDestination.NotificationSettings> {
        TouristNotificationSettingsScreen()
    }
    composable<TouristAccountDestination.LegalAgreements> {
        LegalAgreementsScreen(
            titleResId = R.string.legal_title,
            introResId = R.string.legal_intro,
            legalClauses = touristLegalClauses,
        )
    }
    composable<TouristAccountDestination.HelpSupport> {
        HelpSupportScreen(
            introResId = R.string.support_intro,
            faqEntries = touristFaqEntries,
        )
    }
}

internal fun NavDestination?.touristAccountTitleResId(): Int =
    when {
        this == null || hasRoute<TouristAccountDestination.SavedCards>() -> R.string.saved_cards
        hasRoute<TouristAccountDestination.ChangePassword>() -> R.string.change_password
        hasRoute<TouristAccountDestination.NotificationSettings>() ->
            R.string.notification_settings
        hasRoute<TouristAccountDestination.LegalAgreements>() -> R.string.legal_agreements
        hasRoute<TouristAccountDestination.HelpSupport>() -> R.string.help_support
        else -> R.string.account_settings
}
