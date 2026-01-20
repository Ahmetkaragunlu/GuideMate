package com.ahmetkaragunlu.guidemate.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class TokenManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_USER_ROLE = "user_role"

        private const val KEY_USER_NAME = "user_name"
    }

    private val lock = Any() // Added for thread safety

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "guidemate_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAccessToken(token: String) {
        sharedPreferences.edit { putString(KEY_ACCESS_TOKEN, token) }
    }

    fun getAccessToken(): String? = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)

    fun saveUserName(name: String) {
        sharedPreferences.edit { putString(KEY_USER_NAME, name) }
    }

    fun getUserName(): String? = sharedPreferences.getString(KEY_USER_NAME, null)

    fun saveRefreshToken(token: String) {
        sharedPreferences.edit { putString(KEY_REFRESH_TOKEN, token) }
    }

    fun getRefreshToken(): String? = sharedPreferences.getString(KEY_REFRESH_TOKEN, null)

    fun getDeviceId(): String {
        synchronized(lock) {
            val existingId = sharedPreferences.getString(KEY_DEVICE_ID, null)
            if (existingId != null) return existingId

            val newId = UUID.randomUUID().toString()
            sharedPreferences.edit { putString(KEY_DEVICE_ID, newId) }
            return newId
        }
    }

    fun saveUserRole(role: String) {
        sharedPreferences.edit { putString(KEY_USER_ROLE, role) }
    }

    fun getUserRole(): String? = sharedPreferences.getString(KEY_USER_ROLE, null)

    fun clearTokens() {
        sharedPreferences.edit {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_USER_ROLE)
            remove(KEY_USER_NAME)
        }
    }
}