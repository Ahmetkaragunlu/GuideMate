package com.ahmetkaragunlu.guidemate.wallet.domain.iban

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TurkishBankCatalogTest {
    private val catalog = TurkishBankCatalog()

    @Test
    fun `known provider codes resolve bank names`() {
        assertEquals("Ziraat Bankası", catalog.bankName("00010"))
        assertEquals("Akbank", catalog.bankName("00046"))
        assertEquals("Türkiye İş Bankası", catalog.bankName("00064"))
    }

    @Test
    fun `unknown provider code does not guess a bank`() {
        assertNull(catalog.bankName("99999"))
    }
}
