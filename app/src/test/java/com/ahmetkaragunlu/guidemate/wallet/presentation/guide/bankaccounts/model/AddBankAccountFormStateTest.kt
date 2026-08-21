package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AddBankAccountFormStateTest {
    @Test
    fun `complete valid recognized account can be submitted`() {
        val state =
            AddBankAccountFormState(
                bankName = "Türkiye Cumhuriyet Merkez Bankası",
                accountHolderName = "Ahmet Karagünlü",
                ibanBody = "470000100100000350930001",
                isIbanValid = true,
            )

        assertTrue(state.canSubmit)
    }

    @Test
    fun `valid IBAN with unknown bank cannot be submitted`() {
        val state =
            AddBankAccountFormState(
                accountHolderName = "Ahmet Karagünlü",
                ibanBody = "470000100100000350930001",
                isIbanValid = true,
            )

        assertFalse(state.canSubmit)
    }
}
