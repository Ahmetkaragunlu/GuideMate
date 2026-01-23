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

        val deviceId = tokenManager.getDeviceId()
        requestBuilder.addHeader("X-Device-Id", deviceId)

        val url = originalRequest.url.encodedPath

        val isPublicEndpoint = url.contains("/auth/login") ||
                url.contains("/auth/register") ||
                url.contains("/auth/google") ||
                url.contains("/auth/forgot-password") ||
                url.contains("/auth/reset-password") ||
                url.contains("/auth/confirm") ||
                url.contains("/auth/refresh-token")

        if (!isPublicEndpoint) {
            val token = tokenManager.getAccessToken()
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}