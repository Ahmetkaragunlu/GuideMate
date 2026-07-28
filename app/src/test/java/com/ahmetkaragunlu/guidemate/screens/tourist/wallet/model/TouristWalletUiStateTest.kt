package com.ahmetkaragunlu.guidemate.screens.tourist.wallet.model

import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.PaymentCardAssociation
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.PaymentCardType
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.SavedPaymentCardUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class TouristWalletUiStateTest {
    @Test
    fun `default card stays independent from temporary transaction selection`() {
        val defaultCard = card(cardId = "default", isDefault = true)
        val temporaryCard = card(cardId = "temporary", isDefault = false)
        val state =
            TouristWalletUiState(
                savedCards = listOf(defaultCard, temporaryCard),
                selectedCardId = temporaryCard.cardId,
            )

        assertEquals(defaultCard.cardId, state.defaultCard?.cardId)
        assertEquals(temporaryCard.cardId, state.selectedMethod?.id)
    }

    private fun card(
        cardId: String,
        isDefault: Boolean,
    ): SavedPaymentCardUiModel =
        SavedPaymentCardUiModel(
            cardId = cardId,
            bankName = "Test Bank",
            cardAssociation = PaymentCardAssociation.VISA,
            cardType = PaymentCardType.CREDIT_CARD,
            lastFourDigits = "1234",
            cardHolderName = "Test User",
            expiryMonth = "12",
            expiryYear = "2030",
            isDefault = isDefault,
        )
}
