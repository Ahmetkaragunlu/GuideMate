package com.ahmetkaragunlu.guidemate.media.data.remote.model

import com.google.gson.annotations.SerializedName

data class MediaUploadResponse(
    @SerializedName("mediaAssetId") val mediaAssetId: String,
    @SerializedName("purpose") val purpose: String,
    @SerializedName("status") val status: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("contentType") val contentType: String,
    @SerializedName("sizeBytes") val sizeBytes: Long,
)

data class MediaDeletionResponse(
    @SerializedName("mediaAssetId") val mediaAssetId: String,
    @SerializedName("status") val status: String,
)
