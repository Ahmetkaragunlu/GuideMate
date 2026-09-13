package com.ahmetkaragunlu.guidemate.auth.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.ahmetkaragunlu.guidemate.auth.data.local.preferences.AuthPreferencesDataSource
import com.ahmetkaragunlu.guidemate.auth.data.local.session.AuthSessionManager
import com.ahmetkaragunlu.guidemate.auth.data.local.session.CredentialSessionCleaner
import com.ahmetkaragunlu.guidemate.auth.data.local.session.InMemorySecureSessionStorage
import com.ahmetkaragunlu.guidemate.auth.data.local.session.TokenManager
import com.ahmetkaragunlu.guidemate.auth.data.remote.api.AuthApi
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import com.ahmetkaragunlu.guidemate.auth.domain.validation.EmailPolicy
import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.storage.installation.InstallationIdDataSource
import com.google.gson.Gson
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepositoryImplTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    private lateinit var server: MockWebServer
    private lateinit var scope: CoroutineScope
    private lateinit var authDataStore: DataStore<Preferences>
    private lateinit var tokenManager: TokenManager
    private lateinit var authPreferences: AuthPreferencesDataSource
    private lateinit var authSessionManager: AuthSessionManager
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        server = MockWebServer().apply { start() }
        scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        authDataStore = preferenceDataStore("auth.preferences_pb")
        val installationDataStore = preferenceDataStore("installation.preferences_pb")
        tokenManager = TokenManager(InMemorySecureSessionStorage())
        authPreferences = AuthPreferencesDataSource(authDataStore)
        authSessionManager = AuthSessionManager(tokenManager, authPreferences)
        val api =
            Retrofit
                .Builder()
                .baseUrl(server.url("/"))
                .client(OkHttpClient.Builder().retryOnConnectionFailure(false).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthApi::class.java)
        repository =
            AuthRepositoryImpl(
                api = api,
                tokenManager = tokenManager,
                installationIdDataSource = InstallationIdDataSource(installationDataStore),
                authSessionManager = authSessionManager,
                credentialSessionCleaner = CredentialSessionCleaner {},
                apiErrorParser = ApiErrorParser(Gson()),
                networkExceptionMapper = NetworkExceptionMapper(),
                emailPolicy = EmailPolicy(),
            )
    }

    @After
    fun tearDown() {
        scope.cancel()
        server.shutdown()
    }

    @Test
    fun `successful login normalizes request and stores complete session`() =
        runTest {
            server.enqueue(MockResponse().setResponseCode(200).setBody(successResponseJson()))

            val result = repository.login("  USER@Example.COM ", "password")

            assertTrue(result is DataResult.Success)
            assertEquals(expectedUser(), (result as DataResult.Success).data)
            assertEquals("access-new", tokenManager.getAccessToken())
            assertEquals("refresh-new", tokenManager.getRefreshToken())
            assertEquals(expectedUser(), authPreferences.userState.value)

            val request = server.takeRequest()
            assertEquals("/api/v1/auth/login", request.path)
            assertTrue(request.body.readUtf8().contains("\"email\":\"user@example.com\""))
            val installationId = request.getHeader("X-Installation-Id")
            assertEquals(installationId, UUID.fromString(installationId).toString())
        }

    @Test
    fun `successful response without required refresh token is rejected without partial session`() =
        runTest {
            server.enqueue(
                MockResponse().setResponseCode(200).setBody(successResponseJson(refreshToken = null)),
            )

            val result = repository.login("user@example.com", "password")

            assertEquals(DataResult.Error(AppError.NoResponseFromServer), result)
            assertNull(tokenManager.getAccessToken())
            assertNull(tokenManager.getRefreshToken())
            assertEquals(UserState(), authPreferences.userState.value)
        }

    @Test
    fun `terminal login error clears existing local session`() =
        runTest {
            authSessionManager.saveSession("access-old", "refresh-old", expectedUser())
            server.enqueue(
                MockResponse()
                    .setResponseCode(401)
                    .setBody(
                        """{"code":"UNAUTHORIZED","message":"unauthorized","fieldErrors":[]}""",
                    ),
            )

            val result = repository.login("user@example.com", "wrong-password")

            assertTrue(result is DataResult.Error)
            assertNull(tokenManager.getAccessToken())
            assertNull(tokenManager.getRefreshToken())
            assertEquals(UserState(), authPreferences.userState.value)
            assertFalse(repository.hasStoredSession())
        }

    @Test
    fun `logout clears local session when backend connection fails`() =
        runTest {
            authSessionManager.saveSession("access-old", "refresh-old", expectedUser())
            server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))

            val result = repository.logout()

            assertEquals(DataResult.Success(Unit), result)
            assertNull(tokenManager.getAccessToken())
            assertNull(tokenManager.getRefreshToken())
            assertEquals(UserState(), authPreferences.userState.value)
            assertFalse(repository.hasStoredSession())
        }

    private fun preferenceDataStore(fileName: String): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = scope,
            produceFile = { temporaryFolder.newFile(fileName) },
        )

    private fun expectedUser(): UserState =
        UserState(
            userId = 7L,
            email = "user@example.com",
            firstName = "Ada",
            lastName = "Lovelace",
            isRoleSelected = true,
            role = UserRole.TOURIST,
            avatarMediaId = "avatar-7",
            avatarUrl = "https://cdn.example.com/avatar-7.jpg",
        )

    private fun successResponseJson(refreshToken: String? = "refresh-new"): String =
        """
        {
          "accessToken":"access-new",
          "refreshToken":${refreshToken?.let { "\"$it\"" } ?: "null"},
          "message":null,
          "userId":7,
          "email":"user@example.com",
          "firstName":"Ada",
          "lastName":"Lovelace",
          "roleSelected":true,
          "role":"ROLE_TOURIST",
          "avatar":{
            "mediaAssetId":"avatar-7",
            "imageUrl":"https://cdn.example.com/avatar-7.jpg"
          }
        }
        """.trimIndent()
}
