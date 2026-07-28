package com.ahmetkaragunlu.guidemate.screens.guide.finance.iban

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TurkishIbanValidatorTest {
    private val validator = TurkishIbanValidator()

    @Test
    fun `official TCMB sample is valid`() {
        assertTrue(validator.isValid("TR470000100100000350930001"))
    }

    @Test
    fun `spaces and lowercase country code are normalized`() {
        assertTrue(validator.isValid("tr47 0000 1001 0000 0350 9300 01"))
    }

    @Test
    fun `invalid check digits are rejected`() {
        assertFalse(validator.isValid("TR480000100100000350930001"))
    }

    @Test
    fun `full pasted IBAN is converted to editable body`() {
        assertEquals(
            "470000100100000350930001",
            validator.sanitizeBody("TR47 0000 1001 0000 0350 9300 01"),
        )
    }

    @Test
    fun `provider code is extracted as five digits`() {
        assertEquals("00001", validator.bankCode("TR470000100100000350930001"))
    }
}
