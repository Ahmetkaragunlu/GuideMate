package com.ahmetkaragunlu.guidemate.payment.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface PendingPaymentStorage {
    val paymentId: Flow<String?>

    suspend fun save(paymentId: String)

    suspend fun clear(paymentId: String)

    suspend fun clear()
}

@Singleton
class DataStorePendingPaymentStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PendingPaymentStorage {
    override val paymentId: Flow<String?> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences()) else throw exception
            }.map { preferences -> preferences[PENDING_PAYMENT_ID] }

    override suspend fun save(paymentId: String) {
        dataStore.edit { preferences -> preferences[PENDING_PAYMENT_ID] = paymentId }
    }

    override suspend fun clear(paymentId: String) {
        dataStore.edit { preferences ->
            if (preferences[PENDING_PAYMENT_ID] == paymentId) {
                preferences.remove(PENDING_PAYMENT_ID)
            }
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences -> preferences.remove(PENDING_PAYMENT_ID) }
    }

    private companion object {
        val PENDING_PAYMENT_ID = stringPreferencesKey("pending_payment_id")
    }
}
