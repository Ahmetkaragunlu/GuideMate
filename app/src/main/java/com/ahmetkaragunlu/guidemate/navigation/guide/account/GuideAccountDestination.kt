package com.ahmetkaragunlu.guidemate.navigation.guide.account

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class GuideAccountStart {
    BANK_ACCOUNTS,
    ABOUT,
    CHANGE_PASSWORD,
    NOTIFICATION_SETTINGS,
    LEGAL_AGREEMENTS,
    HELP_SUPPORT,
}

object GuideAccountDestination {
    @Serializable data object BankAccounts

    @Serializable data object About

    @Serializable data object ChangePassword

    @Serializable data object NotificationSettings

    @Serializable data object LegalAgreements

    @Serializable data object HelpSupport
}

internal fun GuideAccountStart.toDestination(): Any =
    when (this) {
        GuideAccountStart.BANK_ACCOUNTS -> GuideAccountDestination.BankAccounts
        GuideAccountStart.ABOUT -> GuideAccountDestination.About
        GuideAccountStart.CHANGE_PASSWORD -> GuideAccountDestination.ChangePassword
        GuideAccountStart.NOTIFICATION_SETTINGS -> GuideAccountDestination.NotificationSettings
        GuideAccountStart.LEGAL_AGREEMENTS -> GuideAccountDestination.LegalAgreements
        GuideAccountStart.HELP_SUPPORT -> GuideAccountDestination.HelpSupport
    }
