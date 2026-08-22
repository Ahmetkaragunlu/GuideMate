package com.ahmetkaragunlu.guidemate.media.data.remote.model

import com.google.gson.annotations.SerializedName

data class MediaReferenceResponseDto(
    @SerializedName("mediaAssetId") val mediaAssetId: String,
    @SerializedName("imageUrl") val imageUrl: String,
)
