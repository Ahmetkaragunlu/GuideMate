package com.ahmetkaragunlu.guidemate.auth.data.remote.session

import com.ahmetkaragunlu.guidemate.auth.data.local.session.InMemorySecureSessionStorage
import com.ahmetkaragunlu.guidemate.auth.data.local.session.TokenManager
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class AuthInterceptorTest {
    private lateinit var backendServer: MockWebServer
    private lateinit var externalServer: MockWebServer
    private lateinit var client: OkHttpClient

    @Before
    fun setUp() {
        backendServer = MockWebServer().apply { start() }
        externalServer = MockWebServer().apply { start() }
        val tokenManager = TokenManager(InMemorySecureSessionStorage())
        tokenManager.saveTokens(accessToken = "access-token", refreshToken = "refresh-token")
        client =
            OkHttpClient
                .Builder()
                .addInterceptor(AuthInterceptor(tokenManager, backendServer.url("/")))
                .build()
    }

    @After
    fun tearDown() {
        backendServer.shutdown()
        externalServer.shutdown()
    }

    @Test
    fun `protected backend request receives access token`() {
        backendServer.enqueue(MockResponse().setResponseCode(200))

        execute(backendServer.url("/api/v1/tours/mine"))

        val request = backendServer.takeRequest(1, TimeUnit.SECONDS)
        assertNotNull(request)
        assertEquals("Bearer access-token", request?.getHeader("Authorization"))
    }

    @Test
    fun `public backend request does not receive access token`() {
        backendServer.enqueue(MockResponse().setResponseCode(200))

        execute(backendServer.url("/api/v1/auth/login"))

        val request = backendServer.takeRequest(1, TimeUnit.SECONDS)
        assertNotNull(request)
        assertNull(request?.getHeader("Authorization"))
    }

    @Test
    fun `external request never receives access token`() {
        externalServer.enqueue(MockResponse().setResponseCode(200))

        execute(externalServer.url("/asset.jpg"))

        val request = externalServer.takeRequest(1, TimeUnit.SECONDS)
        assertNotNull(request)
        assertNull(request?.getHeader("Authorization"))
    }

    private fun execute(url: okhttp3.HttpUrl) {
        client
            .newCall(Request.Builder().url(url).build())
            .execute()
            .use { response -> assertEquals(200, response.code) }
    }
}
