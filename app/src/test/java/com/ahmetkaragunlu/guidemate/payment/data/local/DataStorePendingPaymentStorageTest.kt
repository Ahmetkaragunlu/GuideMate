package com.ahmetkaragunlu.guidemate.payment.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class DataStorePendingPaymentStorageTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    private lateinit var scope: CoroutineScope
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var storage: DataStorePendingPaymentStorage

    @Before
    fun setUp() {
        scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        dataStore =
            PreferenceDataStoreFactory.create(
                scope = scope,
                produceFile = { temporaryFolder.newFile("pending-payment.preferences_pb") },
            )
        storage = DataStorePendingPaymentStorage(dataStore)
    }

    @After
    fun tearDown() {
        scope.cancel()
    }

    @Test
    fun `matching payment id clears persisted value while different id preserves it`() =
        runTest {
            storage.save("payment-1")

            storage.clear("payment-2")
            assertEquals("payment-1", DataStorePendingPaymentStorage(dataStore).paymentId.first())

            storage.clear("payment-1")
            assertNull(storage.paymentId.first())
        }

    @Test
    fun `clear without id removes any pending payment`() =
        runTest {
            storage.save("payment-1")

            storage.clear()

            assertNull(storage.paymentId.first())
        }
}
