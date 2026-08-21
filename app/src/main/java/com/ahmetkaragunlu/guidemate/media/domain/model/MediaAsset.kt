package com.ahmetkaragunlu.guidemate.media.domain.model

data class MediaAsset(
    val mediaAssetId: String,
    val purpose: MediaPurpose,
    val status: MediaStatus,
    val imageUrl: String,
    val contentType: String,
    val sizeBytes: Long,
)
