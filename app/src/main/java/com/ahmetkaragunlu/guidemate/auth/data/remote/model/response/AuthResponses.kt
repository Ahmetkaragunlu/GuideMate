package com.ahmetkaragunlu.guidemate.auth.data.remote.model.response

import com.ahmetkaragunlu.guidemate.auth.data.remote.model.RoleType
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
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
    @SerializedName("avatar") val avatar: MediaReferenceResponseDto?,
)

data class CurrentUserResponse(
    @SerializedName("userId") val userId: Long,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("roleSelected") val isRoleSelected: Boolean,
    @SerializedName("role") val role: RoleType?,
    @SerializedName("avatar") val avatar: MediaReferenceResponseDto?,
)
