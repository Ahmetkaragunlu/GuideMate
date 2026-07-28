package com.ahmetkaragunlu.guidemate.screens.tourist.finance.model

data class TouristFinanceState(
    val balanceMinor: Long = 0,
    val savedCards: List<SavedPaymentCardUiModel> = emptyList(),
    val transactions: List<TouristWalletTransactionUiModel> = emptyList(),
) {
    val defaultCard: SavedPaymentCardUiModel?
        get() = savedCards.firstOrNull { it.isDefault } ?: savedCards.firstOrNull()
}
