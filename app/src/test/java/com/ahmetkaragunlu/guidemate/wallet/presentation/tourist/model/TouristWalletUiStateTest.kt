package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model

import com.ahmetkaragunlu.guidemate.payment.presentation.model.PaymentCardAssociation
import com.ahmetkaragunlu.guidemate.payment.presentation.model.PaymentCardType
import com.ahmetkaragunlu.guidemate.payment.presentation.model.SavedPaymentCardUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class TouristWalletUiStateTest {
    @Test
    fun `provider default card is shown first without local payment selection`() {
        val defaultCard = card(cardId = "default", isDefault = true)
        val state =
            TouristWalletUiState(
                savedCards = listOf(card(cardId = "other", isDefault = false), defaultCard),
            )

        assertEquals(defaultCard.cardId, state.defaultCard?.cardId)
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
            expiryMonth = 12,
            expiryYear = 2030,
            isDefault = isDefault,
        )
}
