package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model

import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.payment.presentation.model.SavedPaymentCardUiModel
import com.ahmetkaragunlu.guidemate.payment.presentation.model.toMoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.wallet.data.mock.tourist.model.TouristWalletTransactionUiModel

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
