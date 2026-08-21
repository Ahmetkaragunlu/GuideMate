package com.ahmetkaragunlu.guidemate.auth.data.remote.session

internal object AuthEndpointPolicy {
    private val publicPaths =
        setOf(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/google",
            "/api/v1/auth/refresh-token",
            "/api/v1/auth/forgot-password",
            "/api/v1/auth/reset-password",
            "/api/v1/auth/reset-password-form",
            "/api/v1/auth/resend-verification",
            "/api/v1/auth/confirm",
        )

    fun requiresAccessToken(path: String): Boolean =
        path !in publicPaths

    fun isRefreshRequest(path: String): Boolean =
        path == "/api/v1/auth/refresh-token"
}
