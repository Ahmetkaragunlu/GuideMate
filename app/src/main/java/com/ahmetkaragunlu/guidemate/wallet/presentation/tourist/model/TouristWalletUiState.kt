package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model

import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.payment.presentation.model.SavedPaymentCardUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.toMoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class TouristWalletUiState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val balanceMinor: Long = 0,
    val currencyCode: String = "USD",
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
