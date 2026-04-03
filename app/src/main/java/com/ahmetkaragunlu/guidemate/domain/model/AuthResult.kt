package com.ahmetkaragunlu.guidemate.domain.model

data class AuthResult(
    val accessToken: String?,
    val refreshToken: String?,
    val isRoleSelected: Boolean,
    val role: UserRole?,
    val firstName: String?,
    val lastName: String?,
)
