package com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.model

data class AddSavedCardFormState(
    val cardNumber: String = "",
    val cardHolderName: String = "",
    val expiryMonth: String = "",
    val expiryYear: String = "",
) {
    val isCardNumberValid: Boolean
        get() = cardNumber.length == CARD_NUMBER_LENGTH

    val isCardHolderNameValid: Boolean
        get() = cardHolderName.trim().isNotEmpty()

    val isExpiryMonthValid: Boolean
        get() = expiryMonth.length == MONTH_LENGTH && expiryMonth.toIntOrNull() in 1..12

    val isExpiryYearValid: Boolean
        get() = expiryYear.length == YEAR_LENGTH && expiryYear.all(Char::isDigit)

    val canSubmit: Boolean
        get() = isCardNumberValid &&
            isCardHolderNameValid &&
            isExpiryMonthValid &&
            isExpiryYearValid

    private companion object {
        const val CARD_NUMBER_LENGTH = 16
        const val MONTH_LENGTH = 2
        const val YEAR_LENGTH = 2
    }
}
