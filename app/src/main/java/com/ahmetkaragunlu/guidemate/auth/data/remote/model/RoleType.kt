package com.ahmetkaragunlu.guidemate.auth.data.remote.model

import com.google.gson.annotations.SerializedName

enum class RoleType {
    @SerializedName("ROLE_TOURIST")
    ROLE_TOURIST,

    @SerializedName("ROLE_GUIDE")
    ROLE_GUIDE,
}
