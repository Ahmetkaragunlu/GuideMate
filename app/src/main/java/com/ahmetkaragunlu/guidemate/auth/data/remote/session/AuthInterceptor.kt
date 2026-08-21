package com.ahmetkaragunlu.guidemate.auth.data.remote.session

import com.ahmetkaragunlu.guidemate.auth.data.local.session.TokenManager
import com.ahmetkaragunlu.guidemate.common.network.ApiBaseUrl
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor
    @Inject
    constructor(
        private val tokenManager: TokenManager,
        @param:ApiBaseUrl private val apiBaseUrl: HttpUrl,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
            if (
                AuthEndpointPolicy.isBackendRequest(originalRequest.url, apiBaseUrl) &&
                    AuthEndpointPolicy.requiresAccessToken(originalRequest.url.encodedPath)
            ) {
                val token = tokenManager.getAccessToken()
                if (!token.isNullOrEmpty()) {
                    requestBuilder.header("Authorization", "Bearer $token")
                }
            }

            return chain.proceed(requestBuilder.build())
        }
    }
