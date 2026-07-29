package com.ahmetkaragunlu.guidemate.data.local

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val secureStringStorage: SecureStringStorage,
) {
    fun saveTokens(
        accessToken: String,
        refreshToken: String?,
    ) {
        val values =
            buildMap {
                put(KEY_ACCESS_TOKEN, accessToken)
                refreshToken?.let { put(KEY_REFRESH_TOKEN, it) }
            }
        secureStringStorage.putAll(values)
    }

    fun getAccessToken(): String? = secureStringStorage.get(KEY_ACCESS_TOKEN)

    fun getRefreshToken(): String? = secureStringStorage.get(KEY_REFRESH_TOKEN)

    fun hasStoredSession(): Boolean =
        !getAccessToken().isNullOrBlank() && !getRefreshToken().isNullOrBlank()

    fun clearTokens() {
        secureStringStorage.remove(KEY_ACCESS_TOKEN, KEY_REFRESH_TOKEN)
    }

    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
