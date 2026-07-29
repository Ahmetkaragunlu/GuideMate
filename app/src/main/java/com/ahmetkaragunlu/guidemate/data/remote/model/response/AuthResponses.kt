package com.ahmetkaragunlu.guidemate.data.remote.model.response

import com.ahmetkaragunlu.guidemate.data.remote.model.RoleType
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("userId") val userId: Long,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("roleSelected") val isRoleSelected: Boolean,
    @SerializedName("role") val role: RoleType?,
)

data class CurrentUserResponse(
    @SerializedName("userId") val userId: Long,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("roleSelected") val isRoleSelected: Boolean,
    @SerializedName("role") val role: RoleType?,
)
