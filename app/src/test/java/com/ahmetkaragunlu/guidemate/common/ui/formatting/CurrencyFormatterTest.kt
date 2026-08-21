package com.ahmetkaragunlu.guidemate.common.ui.formatting

import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CurrencyFormatterTest {
    @Test
    fun `minor units are formatted as USD with locale separators`() {
        assertEquals("$1,500.50", 150_050L.toPlatformCurrencyFromMinorUnit(Locale.US))
        assertEquals("$1.500,50", 150_050L.toPlatformCurrencyFromMinorUnit(Locale.forLanguageTag("tr-TR")))
    }

    @Test
    fun `whole and decimal inputs convert to exact minor units`() {
        assertEquals(150_000L, "1500".toCurrencyMinorUnitsOrNull())
        assertEquals(150_050L, "1500.50".toCurrencyMinorUnitsOrNull())
        assertEquals(150_050L, "1500,50".toCurrencyMinorUnitsOrNull())
    }

    @Test
    fun `invalid currency inputs are rejected`() {
        assertTrue("".isValidCurrencyInput())
        assertTrue("1500,5".isValidCurrencyInput())
        assertFalse("1,500,50".isValidCurrencyInput())
        assertFalse("1500.555".isValidCurrencyInput())
        assertNull("".toCurrencyMinorUnitsOrNull())
    }

    @Test
    fun `minor units convert to locale aware editable input`() {
        assertEquals("1500.5", 150_050L.toCurrencyInput(Locale.US))
        assertEquals("1500,5", 150_050L.toCurrencyInput(Locale.forLanguageTag("tr-TR")))
    }
}
