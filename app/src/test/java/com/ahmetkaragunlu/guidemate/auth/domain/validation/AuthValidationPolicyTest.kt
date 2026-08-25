package com.ahmetkaragunlu.guidemate.auth.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthValidationPolicyTest {
    private val emailPolicy = EmailPolicy()
    private val passwordPolicy = NumericPasswordPolicy()

    @Test
    fun `email is normalized before validation`() {
        assertEquals("user@example.com", emailPolicy.normalize("  USER@Example.COM "))
        assertTrue(emailPolicy.isValid("  USER@Example.COM "))
    }

    @Test
    fun `invalid email shapes are rejected`() {
        assertFalse(emailPolicy.isValid("user@example"))
        assertFalse(emailPolicy.isValid("user example.com"))
        assertFalse(emailPolicy.isValid(""))
    }

    @Test
    fun `numeric password keeps digits and requires minimum length`() {
        assertEquals("12345678", passwordPolicy.sanitize("12ab34-5678"))
        assertTrue(passwordPolicy.isValid("12345678"))
        assertFalse(passwordPolicy.isValid("1234567"))
        assertFalse(passwordPolicy.isValid("1234567a"))
    }
}
