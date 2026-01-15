package com.ahmetkaragunlu.guidemate.data.remote.model


import com.google.gson.annotations.SerializedName


// 1. REQUEST
data class RegisterRequest(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)
data class GoogleLoginRequest(
    @SerializedName("idToken") val idToken: String
)
data class RefreshTokenRequest(
    @SerializedName("token") val token: String
)
data class RoleSelectionRequest(
    @SerializedName("role") val role: RoleType
)
data class ForgotPasswordRequest(
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String
)
data class ResetPasswordRequest(
    @SerializedName("token") val token: String,
    @SerializedName("newPassword") val newPassword: String,
    @SerializedName("confirmPassword") val confirmPassword: String
)


// 2. RESPONSE
data class AuthResponse(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("roleSelected") val isRoleSelected: Boolean,
    @SerializedName("role") val role: String?
)
data class ErrorResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("timestamp") val timestamp: String?
)


// 3. ENUM
enum class RoleType {
    @SerializedName("ROLE_TOURIST")
    ROLE_TOURIST,
    @SerializedName("ROLE_GUIDE")
    ROLE_GUIDE,
    @SerializedName("ROLE_ADMIN")
    ROLE_ADMIN
}