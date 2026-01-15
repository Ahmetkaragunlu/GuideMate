package com.ahmetkaragunlu.guidemate.data.remote.interceptor


import com.ahmetkaragunlu.guidemate.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Intercepts every network request to inject authentication headers.
 * Adds 'Authorization: Bearer <token>' and 'X-Device-Id'.
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // 1. Always add the Device ID (Required by Backend)
        val deviceId = tokenManager.getDeviceId()
        requestBuilder.addHeader("X-Device-Id", deviceId)

        // 2. Add Access Token if it exists (For protected endpoints)
        val token = tokenManager.getAccessToken()
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}