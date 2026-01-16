package com.ahmetkaragunlu.guidemate.data.local

import com.ahmetkaragunlu.guidemate.data.remote.api.AuthApi
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RefreshTokenRequest
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val authApiProvider: Provider<AuthApi>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null
        val path = response.request.url.encodedPath
        if (path.contains("/auth/login") ||
            path.contains("/auth/register") ||
            path.contains("/auth/google")) {
            return null
        }

        synchronized(this) {
            val currentToken = tokenManager.getAccessToken()
            val refreshToken = tokenManager.getRefreshToken() ?: return null
            val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")
            if (currentToken != requestToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }
            try {
                val refreshResponse = authApiProvider.get()
                    .refreshTokenSync(RefreshTokenRequest(refreshToken))
                    .execute()

                return if (refreshResponse.isSuccessful && refreshResponse.body() != null) {
                    val newTokens = refreshResponse.body()!!
                    newTokens.accessToken?.let { tokenManager.saveAccessToken(it) }
                    newTokens.refreshToken?.let { tokenManager.saveRefreshToken(it) }
                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${newTokens.accessToken}")
                        .build()
                } else {
                    tokenManager.clearTokens()
                    null
                }
            } catch (e: Exception) {
                return null
            }
        }
    }
}