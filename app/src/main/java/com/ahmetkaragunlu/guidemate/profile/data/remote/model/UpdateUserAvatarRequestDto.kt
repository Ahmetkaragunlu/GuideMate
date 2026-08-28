package com.ahmetkaragunlu.guidemate.profile.data.remote.model

import com.google.gson.annotations.SerializedName

data class UpdateUserAvatarRequestDto(
    @SerializedName("avatarMediaId") val avatarMediaId: String,
)
