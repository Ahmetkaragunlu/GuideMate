package com.ahmetkaragunlu.guidemate.wallet.data.mock.tourist.model

import com.ahmetkaragunlu.guidemate.payment.presentation.model.SavedPaymentCardUiModel

data class TouristWalletState(
    val balanceMinor: Long = 0,
    val savedCards: List<SavedPaymentCardUiModel> = emptyList(),
    val transactions: List<TouristWalletTransactionUiModel> = emptyList(),
) {
    val defaultCard: SavedPaymentCardUiModel?
        get() = savedCards.firstOrNull { it.isDefault } ?: savedCards.firstOrNull()
}
