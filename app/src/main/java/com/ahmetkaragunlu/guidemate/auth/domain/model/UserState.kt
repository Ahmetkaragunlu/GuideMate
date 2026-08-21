package com.ahmetkaragunlu.guidemate.auth.domain.model

data class UserState(
    val userId: Long? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val isRoleSelected: Boolean = false,
    val role: UserRole? = null,
) {
    val isAuthenticated: Boolean
        get() = userId != null && !email.isNullOrBlank()
}
