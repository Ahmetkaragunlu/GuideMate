package com.ahmetkaragunlu.guidemate.auth.data.local.session

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TokenManagerTest {
    private val storage = InMemorySecureSessionStorage()
    private val tokenManager = TokenManager(storage)

    @Test
    fun `stored session requires both access and refresh tokens`() {
        tokenManager.saveTokens(accessToken = "access-1", refreshToken = null)
        assertFalse(tokenManager.hasStoredSession())

        tokenManager.saveTokens(accessToken = "access-2", refreshToken = "refresh-2")
        assertTrue(tokenManager.hasStoredSession())
        assertEquals("access-2", tokenManager.getAccessToken())
        assertEquals("refresh-2", tokenManager.getRefreshToken())
    }

    @Test
    fun `access token rotation can retain the current refresh token`() {
        tokenManager.saveTokens(accessToken = "access-1", refreshToken = "refresh-1")
        tokenManager.saveTokens(accessToken = "access-2", refreshToken = null)

        assertEquals("access-2", tokenManager.getAccessToken())
        assertEquals("refresh-1", tokenManager.getRefreshToken())
    }

    @Test
    fun `clear removes every session secret`() {
        tokenManager.saveTokens(accessToken = "access", refreshToken = "refresh")

        tokenManager.clearTokens()

        assertNull(tokenManager.getAccessToken())
        assertNull(tokenManager.getRefreshToken())
        assertFalse(tokenManager.hasStoredSession())
    }

    private class InMemorySecureSessionStorage : SecureSessionStorage {
        private val values = mutableMapOf<String, String>()

        override fun get(key: String): String? = values[key]

        override fun putAll(values: Map<String, String>) {
            this.values.putAll(values)
        }

        override fun remove(vararg keys: String) {
            keys.forEach(values::remove)
        }
    }
}
