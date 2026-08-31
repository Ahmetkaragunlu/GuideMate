package com.ahmetkaragunlu.guidemate.common.ui.formatting

import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Test

class RatingFormatterTest {
    @Test
    fun `rating uses one decimal digit with English locale`() {
        assertEquals("4.8", 4.84.toRatingText(Locale.US))
        assertEquals("1.0", 1.0.toRatingText(Locale.US))
        assertEquals("5.0", 5.0.toRatingText(Locale.US))
    }

    @Test
    fun `rating uses locale decimal separator`() {
        assertEquals("4,8", 4.84.toRatingText(Locale.forLanguageTag("tr-TR")))
    }
}
