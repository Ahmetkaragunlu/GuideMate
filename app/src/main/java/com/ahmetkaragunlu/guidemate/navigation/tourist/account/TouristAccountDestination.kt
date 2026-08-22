package com.ahmetkaragunlu.guidemate.navigation.tourist.account

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class TouristAccountStart {
    SAVED_CARDS,
    CHANGE_PASSWORD,
    NOTIFICATION_SETTINGS,
    LEGAL_AGREEMENTS,
    HELP_SUPPORT,
}

object TouristAccountDestination {
    @Serializable data object SavedCards

    @Serializable data object ChangePassword

    @Serializable data object NotificationSettings

    @Serializable data object LegalAgreements

    @Serializable data object HelpSupport
}

internal fun TouristAccountStart.toDestination(): Any =
    when (this) {
        TouristAccountStart.SAVED_CARDS -> TouristAccountDestination.SavedCards
        TouristAccountStart.CHANGE_PASSWORD -> TouristAccountDestination.ChangePassword
        TouristAccountStart.NOTIFICATION_SETTINGS ->
            TouristAccountDestination.NotificationSettings
        TouristAccountStart.LEGAL_AGREEMENTS -> TouristAccountDestination.LegalAgreements
        TouristAccountStart.HELP_SUPPORT -> TouristAccountDestination.HelpSupport
    }
