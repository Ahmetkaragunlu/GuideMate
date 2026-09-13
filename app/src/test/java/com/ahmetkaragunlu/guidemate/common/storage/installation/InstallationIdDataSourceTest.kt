package com.ahmetkaragunlu.guidemate.common.storage.installation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class InstallationIdDataSourceTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    private lateinit var scope: CoroutineScope
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var dataSource: InstallationIdDataSource

    @Before
    fun setUp() {
        scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        dataStore =
            PreferenceDataStoreFactory.create(
                scope = scope,
                produceFile = { temporaryFolder.newFile("installation.preferences_pb") },
            )
        dataSource = InstallationIdDataSource(dataStore)
    }

    @After
    fun tearDown() {
        scope.cancel()
    }

    @Test
    fun `repeated and concurrent calls return one stable valid uuid`() =
        runTest {
            val installationIds =
                List(20) {
                    async(Dispatchers.Default) { dataSource.getOrCreate() }
                }.awaitAll()
            val installationId = installationIds.first()

            assertEquals(1, installationIds.toSet().size)
            assertTrue(installationId.isCanonicalUuid())
            assertEquals(installationId, dataSource.getOrCreate())
        }

    @Test
    fun `invalid persisted value is replaced and remains stable`() =
        runTest {
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey("installation_id")] = "invalid-installation-id"
            }

            val replacement = dataSource.getOrCreate()

            assertNotEquals("invalid-installation-id", replacement)
            assertTrue(replacement.isCanonicalUuid())
            assertEquals(replacement, dataSource.getOrCreate())
        }

    private fun String.isCanonicalUuid(): Boolean =
        runCatching { UUID.fromString(this).toString() == this }.getOrDefault(false)
}
