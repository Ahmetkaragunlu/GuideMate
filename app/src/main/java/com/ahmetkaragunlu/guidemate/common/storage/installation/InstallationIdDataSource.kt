package com.ahmetkaragunlu.guidemate.common.storage.installation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class InstallationIdDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val mutex = Mutex()
    private val preferences =
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }

    suspend fun getOrCreate(): String =
        mutex.withLock {
            preferences
                .first()[INSTALLATION_ID]
                ?.takeIf(::isValidUuid)
                ?: UUID.randomUUID().toString().also { installationId ->
                    dataStore.edit { preferences ->
                        preferences[INSTALLATION_ID] = installationId
                    }
                }
        }

    fun getOrCreateBlocking(): String =
        runBlocking(Dispatchers.IO) {
            getOrCreate()
        }

    private fun isValidUuid(value: String): Boolean =
        runCatching { UUID.fromString(value).toString() == value }.getOrDefault(false)

    private companion object {
        val INSTALLATION_ID = stringPreferencesKey("installation_id")
    }
}
