package com.ahmetkaragunlu.guidemate.media.data.mapper

import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaUploadResponse
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaMapperTest {
    @Test
    fun `maps backend media response to canonical domain model`() {
        val response =
            MediaUploadResponse(
                mediaAssetId = "5bc1fa91-09b3-4473-a325-a67ad60eb764",
                purpose = "TOUR_COVER",
                status = "READY",
                imageUrl = "http://localhost:8080/api/v1/media/5bc1fa91/content",
                contentType = "image/webp",
                sizeBytes = 42L,
            )

        val result = response.toDomain()

        assertEquals(response.mediaAssetId, result.mediaAssetId)
        assertEquals(MediaPurpose.TOUR_COVER, result.purpose)
        assertEquals(MediaStatus.READY, result.status)
        assertEquals(response.imageUrl, result.imageUrl)
        assertEquals(response.contentType, result.contentType)
        assertEquals(response.sizeBytes, result.sizeBytes)
    }
}
