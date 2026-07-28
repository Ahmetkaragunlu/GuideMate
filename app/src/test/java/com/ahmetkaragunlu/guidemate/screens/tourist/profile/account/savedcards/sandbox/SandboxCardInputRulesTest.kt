package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.sandbox

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.PaymentCardAssociation
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model.AddSavedCardFormState
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SandboxCardInputRulesTest {
    @Test
    fun `supported card resolves bank and card association`() {
        val metadata = SandboxCardCatalog.findByCardNumber("5526 0800 0000 0006")

        assertEquals("Akbank", metadata?.bankName)
        assertEquals(PaymentCardAssociation.MASTER_CARD, metadata?.cardAssociation)
    }

    @Test
    fun `valid sandbox form has no errors`() {
        val errors =
            validateSandboxCardForm(
                formState =
                    AddSavedCardFormState(
                        cardNumber = "5526 0800 0000 0006",
                        cardHolderName = "Ahmet Karagünlü",
                        expiryMonth = "08",
                        expiryYear = "30",
                        cvv = "123",
                    ),
                currentMonth = YearMonth.of(2026, 7),
            )

        assertFalse(errors.hasError)
    }

    @Test
    fun `unknown card is rejected even when luhn is valid`() {
        val errors =
            validateSandboxCardForm(
                formState =
                    AddSavedCardFormState(
                        cardNumber = "4111 1111 1111 1111",
                        cardHolderName = "Ahmet Karagünlü",
                        expiryMonth = "08",
                        expiryYear = "30",
                        cvv = "123",
                    ),
                currentMonth = YearMonth.of(2026, 7),
            )

        assertTrue(errors.hasError)
        assertEquals(R.string.sandbox_card_not_supported, errors.cardNumberErrorResId)
        assertNull(errors.expiryMonthErrorResId)
        assertNull(errors.expiryYearErrorResId)
    }
}
