package com.ahmetkaragunlu.guidemate.auth.data.local.session

import com.ahmetkaragunlu.guidemate.auth.data.local.preferences.AuthPreferencesDataSource
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class AuthSessionManager @Inject constructor(
    private val tokenManager: TokenManager,
    private val preferences: AuthPreferencesDataSource,
) {
    private val sessionMutex = Mutex()

    suspend fun saveSession(
        accessToken: String,
        refreshToken: String?,
        userState: UserState,
    ) = sessionMutex.withLock {
        tokenManager.saveTokens(accessToken, refreshToken)
        preferences.saveUser(userState)
    }

    suspend fun saveUser(userState: UserState) =
        sessionMutex.withLock {
            preferences.saveUser(userState)
        }

    suspend fun clearSession() =
        sessionMutex.withLock {
            tokenManager.clearTokens()
            preferences.clearUser()
        }

    fun clearSessionBlocking() {
        runBlocking(Dispatchers.IO) {
            clearSession()
        }
    }

    fun hasStoredSession(): Boolean = tokenManager.hasStoredSession()
}
