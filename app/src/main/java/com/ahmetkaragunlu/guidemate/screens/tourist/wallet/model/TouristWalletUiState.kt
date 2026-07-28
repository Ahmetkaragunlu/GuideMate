package com.ahmetkaragunlu.guidemate.screens.tourist.wallet.model

import com.ahmetkaragunlu.guidemate.screens.common.moneyaction.model.MoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.SavedPaymentCardUiModel
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.TouristWalletTransactionUiModel
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.toMoneyActionMethodUi

data class TouristWalletUiState(
    val balanceMinor: Long = 0,
    val savedCards: List<SavedPaymentCardUiModel> = emptyList(),
    val transactions: List<TouristWalletTransactionUiModel> = emptyList(),
    val topUpAmount: String = "",
    val selectedCardId: String? = null,
) {
    val defaultCard: SavedPaymentCardUiModel?
        get() = savedCards.firstOrNull { it.isDefault } ?: savedCards.firstOrNull()

    val selectedMethod: MoneyActionMethodUi?
        get() =
            savedCards
                .firstOrNull { it.cardId == selectedCardId }
                ?.toMoneyActionMethodUi()
}
