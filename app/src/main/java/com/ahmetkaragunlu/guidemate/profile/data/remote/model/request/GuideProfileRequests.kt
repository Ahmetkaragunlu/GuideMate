package com.ahmetkaragunlu.guidemate.profile.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class UpdateGuideProfileRequest(
    @SerializedName("specialtyTitle") val specialtyTitle: String,
    @SerializedName("biography") val biography: String,
    @SerializedName("languageCodes") val languageCodes: List<String>,
)
