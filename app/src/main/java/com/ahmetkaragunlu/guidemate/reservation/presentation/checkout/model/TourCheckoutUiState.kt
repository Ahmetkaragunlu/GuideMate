package com.ahmetkaragunlu.guidemate.reservation.presentation.checkout.model

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrency
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import com.ahmetkaragunlu.guidemate.payment.presentation.model.PaymentLaunch

data class TourCheckoutUiState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val sessionId: String = "",
    val tourTitle: String = "",
    val date: String = "",
    val location: String = "",
    val unitPriceMinor: Long = 0,
    val participantCount: Int = 1,
    val availableCapacity: Int = 0,
    val walletBalanceMinor: Long = 0,
    val walletCurrencyCode: String = "USD",
    val chargeCurrencies: List<CheckoutCurrency> = emptyList(),
    val selectedChargeCurrencyCode: String? = null,
    val quote: PaymentQuote? = null,
    val selectedMethod: PaymentMethod = PaymentMethod.HOSTED_CARD,
    val termsAccepted: Boolean = false,
    val isPaymentActionInProgress: Boolean = false,
    val paymentActionError: String? = null,
    val paymentLaunch: PaymentLaunch? = null,
    @param:StringRes val validationErrorResId: Int? = null,
) {
    val totalMinor: Long
        get() = unitPriceMinor * participantCount

    val canDecreaseParticipants: Boolean
        get() = participantCount > 1

    val canIncreaseParticipants: Boolean
        get() = participantCount < availableCapacity
}
