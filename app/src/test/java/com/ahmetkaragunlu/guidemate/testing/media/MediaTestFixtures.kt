package com.ahmetkaragunlu.guidemate.testing.media

import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaAsset
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaStatus
import com.ahmetkaragunlu.guidemate.media.domain.repository.MediaRepository

class FakeMediaRepository : MediaRepository {
    var uploadResult: DataResult<MediaAsset> = DataResult.Success(testMediaAsset())
    var uploadedUri: String? = null
    var uploadedPurpose: MediaPurpose? = null
    var deletedMediaIds = mutableListOf<String>()

    override suspend fun uploadImage(
        localUri: String,
        purpose: MediaPurpose,
    ): DataResult<MediaAsset> {
        uploadedUri = localUri
        uploadedPurpose = purpose
        return uploadResult
    }

    override suspend fun deleteUnreferenced(mediaAssetId: String): DataResult<Unit> {
        deletedMediaIds += mediaAssetId
        return DataResult.Success(Unit)
    }
}

fun testMediaAsset(): MediaAsset =
    MediaAsset(
        mediaAssetId = "media-1",
        purpose = MediaPurpose.TOUR_COVER,
        status = MediaStatus.READY,
        imageUrl = "https://example.com/media.jpg",
        contentType = "image/jpeg",
        sizeBytes = 1_024,
    )
