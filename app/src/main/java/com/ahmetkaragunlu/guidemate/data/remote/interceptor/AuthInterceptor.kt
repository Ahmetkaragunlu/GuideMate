package com.ahmetkaragunlu.guidemate.data.remote.interceptor

import com.ahmetkaragunlu.guidemate.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor
    @Inject
    constructor(
        private val tokenManager: TokenManager,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
            if (AuthEndpointPolicy.requiresAccessToken(originalRequest.url.encodedPath)) {
                val token = tokenManager.getAccessToken()
                if (!token.isNullOrEmpty()) {
                    requestBuilder.header("Authorization", "Bearer $token")
                }
            }

            return chain.proceed(requestBuilder.build())
        }
    }
