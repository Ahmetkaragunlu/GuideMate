package com.ahmetkaragunlu.guidemate.auth.data.local.session

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val sessionStorage: SecureSessionStorage,
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
        sessionStorage.putAll(values)
    }

    fun getAccessToken(): String? = sessionStorage.get(KEY_ACCESS_TOKEN)

    fun getRefreshToken(): String? = sessionStorage.get(KEY_REFRESH_TOKEN)

    fun hasStoredSession(): Boolean =
        !getAccessToken().isNullOrBlank() && !getRefreshToken().isNullOrBlank()

    fun clearTokens() {
        sessionStorage.remove(KEY_ACCESS_TOKEN, KEY_REFRESH_TOKEN)
    }

    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
