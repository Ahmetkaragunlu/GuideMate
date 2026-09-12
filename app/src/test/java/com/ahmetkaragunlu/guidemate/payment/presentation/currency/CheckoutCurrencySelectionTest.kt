package com.ahmetkaragunlu.guidemate.payment.presentation.currency

import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrency
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutCurrencySelectionTest {
    @Test
    fun `device currency is preferred when supported`() {
        val currencies = currencies(base = "USD", supported = listOf("USD", "TRY", "EUR"))

        assertEquals("TRY", currencies.preferredChargeCurrencyCode(deviceCurrencyCode = "TRY"))
    }

    @Test
    fun `base currency is fallback when device currency is unsupported`() {
        val currencies = currencies(base = "USD", supported = listOf("TRY", "USD"))

        assertEquals("USD", currencies.preferredChargeCurrencyCode(deviceCurrencyCode = "GBP"))
    }

    @Test
    fun `first supported currency is final fallback`() {
        val currencies = currencies(base = "USD", supported = listOf("TRY", "EUR"))

        assertEquals("TRY", currencies.preferredChargeCurrencyCode(deviceCurrencyCode = null))
    }

    @Test
    fun `empty supported currencies returns null`() {
        val currencies = currencies(base = "USD", supported = emptyList())

        assertEquals(null, currencies.preferredChargeCurrencyCode(deviceCurrencyCode = "USD"))
    }

    private fun currencies(
        base: String,
        supported: List<String>,
    ) = CheckoutCurrencies(
        baseCurrencyCode = base,
        chargeCurrencies = supported.map { CheckoutCurrency(it, fractionDigits = 2) },
    )
}
