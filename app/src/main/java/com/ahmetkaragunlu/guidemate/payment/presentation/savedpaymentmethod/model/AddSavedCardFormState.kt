package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.model

import androidx.annotation.StringRes

data class AddSavedCardFormState(
    val cardNumber: String = "",
    val cardHolderName: String = "",
    val expiryMonth: String = "",
    val expiryYear: String = "",
    val cvv: String = "",
)

data class AddSavedCardFormErrors(
    @param:StringRes val cardNumberErrorResId: Int? = null,
    @param:StringRes val cardHolderErrorResId: Int? = null,
    @param:StringRes val expiryMonthErrorResId: Int? = null,
    @param:StringRes val expiryYearErrorResId: Int? = null,
    @param:StringRes val cvvErrorResId: Int? = null,
) {
    val hasError: Boolean
        get() =
            cardNumberErrorResId != null ||
                cardHolderErrorResId != null ||
                expiryMonthErrorResId != null ||
                expiryYearErrorResId != null ||
                cvvErrorResId != null
}
