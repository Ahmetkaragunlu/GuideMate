package com.ahmetkaragunlu.guidemate.data.remote.model


import com.google.gson.annotations.SerializedName






// 3. ENUM
enum class RoleType {
    @SerializedName("ROLE_TOURIST")
    ROLE_TOURIST,
    @SerializedName("ROLE_GUIDE")
    ROLE_GUIDE,
    @SerializedName("ROLE_ADMIN")
    ROLE_ADMIN
}