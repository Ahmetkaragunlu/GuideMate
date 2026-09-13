package com.ahmetkaragunlu.guidemate.auth.data.local.session

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidKeystoreSessionStorageTest {
    @Test
    fun storesOverwritesAndRemovesEncryptedSessionValues() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val storage = AndroidKeystoreSessionStorage(context)
        val suffix = System.nanoTime().toString()
        val accessKey = "test_access_$suffix"
        val refreshKey = "test_refresh_$suffix"

        try {
            storage.putAll(mapOf(accessKey to "access-1", refreshKey to "refresh-1"))
            assertEquals("access-1", storage.get(accessKey))
            assertEquals("refresh-1", storage.get(refreshKey))

            storage.putAll(mapOf(accessKey to "access-2"))
            assertEquals("access-2", storage.get(accessKey))
            assertEquals("refresh-1", storage.get(refreshKey))

            storage.remove(accessKey, refreshKey)
            assertNull(storage.get(accessKey))
            assertNull(storage.get(refreshKey))
        } finally {
            storage.remove(accessKey, refreshKey)
        }
    }
}
