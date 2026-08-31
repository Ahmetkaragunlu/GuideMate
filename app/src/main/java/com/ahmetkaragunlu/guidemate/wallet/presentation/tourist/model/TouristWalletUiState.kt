package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model

import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrency
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import com.ahmetkaragunlu.guidemate.payment.presentation.model.SavedPaymentCardUiModel
import com.ahmetkaragunlu.guidemate.payment.presentation.model.PaymentLaunch
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class TouristWalletUiState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val balanceMinor: Long = 0,
    val currencyCode: String = "USD",
    val savedCards: List<SavedPaymentCardUiModel> = emptyList(),
    val transactions: List<TouristWalletTransactionUiModel> = emptyList(),
    val topUpAmount: String = "",
    val chargeCurrencies: List<CheckoutCurrency> = emptyList(),
    val selectedChargeCurrencyCode: String? = null,
    val topUpQuote: PaymentQuote? = null,
    val isPaymentActionInProgress: Boolean = false,
    val paymentActionError: String? = null,
    val paymentLaunch: PaymentLaunch? = null,
) {
    val defaultCard: SavedPaymentCardUiModel?
        get() = savedCards.firstOrNull { it.isDefault } ?: savedCards.firstOrNull()

}
