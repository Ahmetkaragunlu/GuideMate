package com.ahmetkaragunlu.guidemate.review.data.mapper

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
import com.ahmetkaragunlu.guidemate.review.data.remote.model.TourReviewResponseDto
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Test

class TourReviewMapperTest {
    @Test
    fun `maps public review page without losing reviewer metadata`() {
        val response =
            ApiPageResponse(
                content =
                    listOf(
                        TourReviewResponseDto(
                            reviewId = "review-1",
                            reviewerDisplayName = "Elif Demir",
                            reviewerAvatar =
                                MediaReferenceResponseDto(
                                    mediaAssetId = "avatar-1",
                                    imageUrl = "https://example.com/avatar",
                                ),
                            rating = 5,
                            comment = "Harika bir deneyimdi.",
                            submittedAt = "2026-08-20T10:00:00Z",
                        ),
                    ),
                page = 0,
                size = 20,
                totalElements = 1,
                totalPages = 1,
                isFirst = true,
                isLast = true,
            )

        val review = response.toDomain().items.single()

        assertEquals("review-1", review.id)
        assertEquals("Elif Demir", review.reviewerName)
        assertEquals("https://example.com/avatar", review.reviewerImageUrl)
        assertEquals(Instant.parse("2026-08-20T10:00:00Z"), review.submittedAt)
    }
}
