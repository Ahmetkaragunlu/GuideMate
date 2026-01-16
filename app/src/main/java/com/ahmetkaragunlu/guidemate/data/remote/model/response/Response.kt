package com.ahmetkaragunlu.guidemate.data.remote.model.response

import com.google.gson.annotations.SerializedName


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
