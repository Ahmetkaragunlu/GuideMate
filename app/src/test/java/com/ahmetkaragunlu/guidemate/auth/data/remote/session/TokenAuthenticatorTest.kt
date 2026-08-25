package com.ahmetkaragunlu.guidemate.auth.data.remote.session

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.ahmetkaragunlu.guidemate.auth.data.local.preferences.AuthPreferencesDataSource
import com.ahmetkaragunlu.guidemate.auth.data.local.session.AuthSessionManager
import com.ahmetkaragunlu.guidemate.auth.data.local.session.SecureSessionStorage
import com.ahmetkaragunlu.guidemate.auth.data.local.session.TokenManager
import com.ahmetkaragunlu.guidemate.auth.data.remote.api.AuthApi
import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.storage.installation.InstallationIdDataSource
import com.google.gson.Gson
import javax.inject.Provider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokenAuthenticatorTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    private lateinit var server: MockWebServer
    private lateinit var scope: CoroutineScope
    private lateinit var tokenManager: TokenManager
    private lateinit var client: OkHttpClient

    @Before
    fun setUp() {
        server = MockWebServer().apply { start() }
        scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        tokenManager = TokenManager(InMemorySecureSessionStorage())
        tokenManager.saveTokens("access-old", "refresh-old")

        val authDataStore =
            PreferenceDataStoreFactory.create(
                scope = scope,
                produceFile = { temporaryFolder.newFile("auth.preferences_pb") },
            )
        val installationDataStore =
            PreferenceDataStoreFactory.create(
                scope = scope,
                produceFile = { temporaryFolder.newFile("installation.preferences_pb") },
            )
        val authSessionManager =
            AuthSessionManager(tokenManager, AuthPreferencesDataSource(authDataStore))
        val authApi =
            Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthApi::class.java)
        val authenticator =
            TokenAuthenticator(
                tokenManager = tokenManager,
                installationIdDataSource = InstallationIdDataSource(installationDataStore),
                authSessionManager = authSessionManager,
                authApiProvider = Provider { authApi },
                apiErrorParser = ApiErrorParser(Gson()),
                apiBaseUrl = server.url("/"),
            )
        client = OkHttpClient.Builder().authenticator(authenticator).build()
    }

    @After
    fun tearDown() {
        scope.cancel()
        server.shutdown()
    }

    @Test
    fun `401 refresh rotates tokens and retries original request once`() {
        server.enqueue(MockResponse().setResponseCode(401))
        server.enqueue(MockResponse().setResponseCode(200).setBody(authResponseJson()))
        server.enqueue(MockResponse().setResponseCode(200).setBody("ok"))

        client.newCall(protectedRequest()).execute().use { response ->
            assertEquals(200, response.code)
        }

        val original = server.takeRequest()
        val refresh = server.takeRequest()
        val retried = server.takeRequest()
        assertEquals("Bearer access-old", original.getHeader("Authorization"))
        assertEquals("/api/v1/auth/refresh-token", refresh.path)
        assertEquals("Bearer access-new", retried.getHeader("Authorization"))
        assertEquals("access-new", tokenManager.getAccessToken())
        assertEquals("refresh-new", tokenManager.getRefreshToken())
    }

    @Test
    fun `second unauthorized response does not create a refresh loop`() {
        server.enqueue(MockResponse().setResponseCode(401))
        server.enqueue(MockResponse().setResponseCode(200).setBody(authResponseJson()))
        server.enqueue(MockResponse().setResponseCode(401))

        client.newCall(protectedRequest()).execute().use { response ->
            assertEquals(401, response.code)
        }

        assertEquals(3, server.requestCount)
    }

    @Test
    fun `terminal refresh failure clears local tokens`() {
        server.enqueue(MockResponse().setResponseCode(401))
        server.enqueue(
            MockResponse()
                .setResponseCode(401)
                .setBody(
                    """{"code":"REFRESH_TOKEN_EXPIRED","message":"expired","fieldErrors":[]}""",
                ),
        )

        client.newCall(protectedRequest()).execute().use { response ->
            assertEquals(401, response.code)
        }

        assertNull(tokenManager.getAccessToken())
        assertNull(tokenManager.getRefreshToken())
        assertEquals(2, server.requestCount)
    }

    private fun protectedRequest(): Request =
        Request.Builder()
            .url(server.url("/api/v1/tours/mine"))
            .header("Authorization", "Bearer access-old")
            .build()

    private fun authResponseJson(): String =
        """
        {
          "accessToken":"access-new",
          "refreshToken":"refresh-new",
          "message":null,
          "userId":7,
          "email":"user@example.com",
          "firstName":"Ada",
          "lastName":"Lovelace",
          "roleSelected":true,
          "role":"ROLE_TOURIST"
        }
        """.trimIndent()

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
