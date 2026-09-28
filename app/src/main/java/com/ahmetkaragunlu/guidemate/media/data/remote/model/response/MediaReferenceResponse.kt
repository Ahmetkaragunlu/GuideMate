package com.ahmetkaragunlu.guidemate.media.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class MediaReferenceResponse(
    @SerializedName("mediaAssetId") val mediaAssetId: String,
    @SerializedName("imageUrl") val imageUrl: String,
)
