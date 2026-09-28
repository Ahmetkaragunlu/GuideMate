package com.ahmetkaragunlu.guidemate.profile.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class UpdateUserAvatarRequest(
    @SerializedName("avatarMediaId") val avatarMediaId: String,
)
