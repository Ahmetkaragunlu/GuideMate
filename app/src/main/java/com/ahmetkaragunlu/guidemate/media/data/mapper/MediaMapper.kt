package com.ahmetkaragunlu.guidemate.media.data.mapper

import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaUploadResponse
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaAsset
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaStatus

internal fun MediaUploadResponse.toDomain(): MediaAsset =
    MediaAsset(
        mediaAssetId = mediaAssetId,
        purpose = MediaPurpose.valueOf(purpose),
        status = MediaStatus.valueOf(status),
        imageUrl = imageUrl,
        contentType = contentType,
        sizeBytes = sizeBytes,
    )
