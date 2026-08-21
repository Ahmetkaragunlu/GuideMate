package com.ahmetkaragunlu.guidemate.media.domain.repository

import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaAsset
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose

interface MediaRepository {
    suspend fun uploadImage(
        localUri: String,
        purpose: MediaPurpose,
    ): DataResult<MediaAsset>

    suspend fun deleteUnreferenced(mediaAssetId: String): DataResult<Unit>
}
