package com.ahmetkaragunlu.guidemate.auth.data.remote.session

import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthEndpointPolicyTest {
    private val apiBaseUrl = "http://192.168.1.10:8080/".toHttpUrl()

    @Test
    fun `accepts only the configured backend origin`() {
        assertTrue(
            AuthEndpointPolicy.isBackendRequest(
                "http://192.168.1.10:8080/api/v1/media/1/content".toHttpUrl(),
                apiBaseUrl,
            ),
        )
        assertFalse(
            AuthEndpointPolicy.isBackendRequest(
                "https://cdn.example.com/api/v1/media/1/content".toHttpUrl(),
                apiBaseUrl,
            ),
        )
        assertFalse(
            AuthEndpointPolicy.isBackendRequest(
                "http://192.168.1.10:9090/api/v1/media/1/content".toHttpUrl(),
                apiBaseUrl,
            ),
        )
    }

    @Test
    fun `public and refresh endpoints never request access token authentication`() {
        assertFalse(AuthEndpointPolicy.requiresAccessToken("/api/v1/auth/login"))
        assertFalse(AuthEndpointPolicy.requiresAccessToken("/api/v1/auth/forgot-password"))
        assertTrue(AuthEndpointPolicy.requiresAccessToken("/api/v1/tours/mine"))
        assertTrue(AuthEndpointPolicy.isRefreshRequest("/api/v1/auth/refresh-token"))
    }
}
