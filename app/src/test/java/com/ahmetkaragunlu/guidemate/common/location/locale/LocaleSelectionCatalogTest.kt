package com.ahmetkaragunlu.guidemate.common.location.locale

import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LocaleSelectionCatalogTest {
    @Test
    fun `language catalog contains one canonical entry per code`() {
        val languages = LocaleSelectionCatalog.languages(Locale.ENGLISH)

        assertEquals(languages.size, languages.map { it.code }.toSet().size)
        assertTrue(languages.size >= 70)
    }

    @Test
    fun `representative flags do not create regional language variants`() {
        val languages = LocaleSelectionCatalog.languages(Locale.ENGLISH)

        assertEquals("🇬🇧", languages.single { it.code == "en" }.flagEmoji)
        assertEquals("🇪🇸", languages.single { it.code == "es" }.flagEmoji)
        assertEquals("🇵🇹", languages.single { it.code == "pt" }.flagEmoji)
        assertEquals(1, languages.count { it.code == "pt" })
    }

    @Test
    fun `language display name follows requested app locale`() {
        val english = LocaleSelectionCatalog.language("en", Locale.ENGLISH)
        val turkish = LocaleSelectionCatalog.language("en", Locale.forLanguageTag("tr"))

        assertNotNull(english)
        assertNotNull(turkish)
        assertEquals("English", english?.displayName)
        assertEquals("İngilizce", turkish?.displayName)
    }
}
