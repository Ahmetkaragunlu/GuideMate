package com.ahmetkaragunlu.guidemate.navigation.tourist.account

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.ChangePasswordScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.account.helpsupport.HelpSupportScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.account.legalagreements.LegalAgreementsScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.tourist.account.helpsupport.model.faqEntries
import com.ahmetkaragunlu.guidemate.profile.presentation.tourist.account.legalagreements.model.legalClauses
import com.ahmetkaragunlu.guidemate.notification.presentation.tourist.settings.NotificationSettingsScreen
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.AddSavedCardScreen
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.SavedCardsScreen

internal fun NavGraphBuilder.touristAccountNavGraph(accountNavController: NavController) {
    composable<TouristAccountDestination.SavedCards> { backStackEntry ->
        val cardAdded =
            backStackEntry.savedStateHandle
                .getStateFlow(CARD_ADDED_RESULT, false)
                .collectAsStateWithLifecycle()

        SavedCardsScreen(
            showCardAddedMessage = cardAdded.value,
            onCardAddedMessageShown = {
                backStackEntry.savedStateHandle[CARD_ADDED_RESULT] = false
            },
            onNavigateToAddCard = {
                accountNavController.navigateTo(TouristAccountDestination.AddSavedCard)
            },
        )
    }
    composable<TouristAccountDestination.AddSavedCard> {
        AddSavedCardScreen(
            onCardAdded = {
                accountNavController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(CARD_ADDED_RESULT, true)
                accountNavController.navigateUp()
            },
        )
    }
    composable<TouristAccountDestination.ChangePassword> {
        ChangePasswordScreen()
    }
    composable<TouristAccountDestination.NotificationSettings> {
        NotificationSettingsScreen()
    }
    composable<TouristAccountDestination.LegalAgreements> {
        LegalAgreementsScreen(
            titleResId = R.string.legal_title,
            introResId = R.string.legal_intro,
            legalClauses = legalClauses,
        )
    }
    composable<TouristAccountDestination.HelpSupport> {
        HelpSupportScreen(
            introResId = R.string.support_intro,
            faqEntries = faqEntries,
        )
    }
}

internal fun NavDestination?.touristAccountTitleResId(): Int =
    when {
        this == null || hasRoute<TouristAccountDestination.SavedCards>() -> R.string.saved_cards
        hasRoute<TouristAccountDestination.AddSavedCard>() -> R.string.add_card
        hasRoute<TouristAccountDestination.ChangePassword>() -> R.string.change_password
        hasRoute<TouristAccountDestination.NotificationSettings>() ->
            R.string.notification_settings
        hasRoute<TouristAccountDestination.LegalAgreements>() -> R.string.legal_agreements
        hasRoute<TouristAccountDestination.HelpSupport>() -> R.string.help_support
        else -> R.string.account_settings
    }

private const val CARD_ADDED_RESULT = "cardAdded"
