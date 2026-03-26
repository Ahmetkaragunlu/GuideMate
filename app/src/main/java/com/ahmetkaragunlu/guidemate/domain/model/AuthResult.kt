package com.ahmetkaragunlu.guidemate.domain.model


data class AuthResult(
    val accessToken: String?,
    val refreshToken: String?,
    val isRoleSelected: Boolean,
    val role: String?,
    val firstName: String?,
    val lastName: String?
)