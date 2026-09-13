package com.ahmetkaragunlu.guidemate.auth.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class AuthPreferencesDataSourceTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    private lateinit var scope: CoroutineScope
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var dataSource: AuthPreferencesDataSource

    @Before
    fun setUp() {
        scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        dataStore =
            PreferenceDataStoreFactory.create(
                scope = scope,
                produceFile = { temporaryFolder.newFile("auth.preferences_pb") },
            )
        dataSource = AuthPreferencesDataSource(dataStore)
    }

    @After
    fun tearDown() {
        scope.cancel()
    }

    @Test
    fun `save persists every user field and updates observable state`() =
        runTest {
            val user = authenticatedGuide()

            dataSource.saveUser(user)

            assertEquals(user, dataSource.userState.value)
            assertEquals(user, AuthPreferencesDataSource(dataStore).restoreUser())
        }

    @Test
    fun `saving nullable fields removes stale optional values`() =
        runTest {
            dataSource.saveUser(authenticatedGuide())
            val userWithoutOptionalFields =
                UserState(
                    userId = 42L,
                    email = "guide@example.com",
                    isRoleSelected = false,
                )

            dataSource.saveUser(userWithoutOptionalFields)

            val restored = AuthPreferencesDataSource(dataStore).restoreUser()
            assertEquals(userWithoutOptionalFields, restored)
            assertNull(restored.firstName)
            assertNull(restored.lastName)
            assertNull(restored.role)
            assertNull(restored.avatarMediaId)
            assertNull(restored.avatarUrl)
        }

    @Test
    fun `clear removes user but preserves onboarding preference`() =
        runTest {
            dataSource.completeOnboarding()
            dataSource.saveUser(authenticatedGuide())

            dataSource.clearUser()

            assertEquals(UserState(), dataSource.userState.value)
            assertEquals(UserState(), AuthPreferencesDataSource(dataStore).restoreUser())
            assertFalse(dataSource.userState.value.isAuthenticated)
            assertTrue(dataSource.isOnboardingCompleted())
        }

    private fun authenticatedGuide(): UserState =
        UserState(
            userId = 42L,
            email = "guide@example.com",
            firstName = "Ada",
            lastName = "Lovelace",
            isRoleSelected = true,
            role = UserRole.GUIDE,
            avatarMediaId = "avatar-42",
            avatarUrl = "https://cdn.example.com/avatar-42.jpg",
        )
}
