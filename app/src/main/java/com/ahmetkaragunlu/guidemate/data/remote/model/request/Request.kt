package com.ahmetkaragunlu.guidemate.data.remote.model.request

import com.ahmetkaragunlu.guidemate.data.remote.model.RoleType
import com.google.gson.annotations.SerializedName

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