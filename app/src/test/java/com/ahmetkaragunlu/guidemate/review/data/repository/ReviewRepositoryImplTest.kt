package com.ahmetkaragunlu.guidemate.review.data.repository

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.network.testApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.review.data.remote.api.ReviewApi
import com.ahmetkaragunlu.guidemate.review.data.remote.model.ReviewSubmissionRequestDto
import com.ahmetkaragunlu.guidemate.review.data.remote.model.SubmittedReviewResponseDto
import com.ahmetkaragunlu.guidemate.review.data.remote.model.TourReviewResponseDto
import com.ahmetkaragunlu.guidemate.review.domain.model.ReviewSubmissionInput
import java.time.Instant
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class ReviewRepositoryImplTest {
    @Test
    fun `submit forwards reservation and normalized review then emits refresh`() = runTest {
        val api = FakeReviewApi()
        val repository = ReviewRepositoryImpl(api, testApiCallExecutor())
        val change = async(start = CoroutineStart.UNDISPATCHED) { repository.reviewChanges.first() }

        val result =
            repository.submitReview(
                reservationId = "reservation-1",
                input = ReviewSubmissionInput(rating = 5, comment = "  Great tour  "),
            )

        assertTrue(result is DataResult.Success)
        assertEquals("reservation-1", api.reservationId)
        assertEquals(5, api.submission?.rating)
        assertEquals("Great tour", api.submission?.comment)
        change.await()
    }

    @Test
    fun `tour review page keeps request pagination`() = runTest {
        val api = FakeReviewApi()
        val repository = ReviewRepositoryImpl(api, testApiCallExecutor())

        val result = repository.getTourReviews(tourId = "tour-1", page = 3, size = 10)

        assertTrue(result is DataResult.Success)
        assertEquals("tour-1", api.tourId)
        assertEquals(3, api.page)
        assertEquals(10, api.size)
        assertEquals("reviewer", (result as DataResult.Success).data.items.single().reviewerName)
    }

    private class FakeReviewApi : ReviewApi {
        var reservationId: String? = null
        var submission: ReviewSubmissionRequestDto? = null
        var tourId: String? = null
        var page: Int? = null
        var size: Int? = null

        override suspend fun submitReview(
            reservationId: String,
            request: ReviewSubmissionRequestDto,
        ): Response<SubmittedReviewResponseDto> {
            this.reservationId = reservationId
            submission = request
            return Response.success(
                SubmittedReviewResponseDto(
                    reviewId = "review-1",
                    rating = request.rating,
                    comment = request.comment,
                    submittedAt = TEST_INSTANT.toString(),
                ),
            )
        }

        override suspend fun getTourReviews(
            tourId: String,
            page: Int,
            size: Int,
        ): Response<ApiPageResponse<TourReviewResponseDto>> {
            this.tourId = tourId
            this.page = page
            this.size = size
            return Response.success(
                ApiPageResponse(
                    content =
                        listOf(
                            TourReviewResponseDto(
                                reviewId = "review-1",
                                reviewerDisplayName = "reviewer",
                                reviewerAvatar = null,
                                rating = 5,
                                comment = "Great",
                                submittedAt = TEST_INSTANT.toString(),
                            ),
                        ),
                    page = page,
                    size = size,
                    totalElements = 1,
                    totalPages = 1,
                    isFirst = false,
                    isLast = true,
                ),
            )
        }

        private companion object {
            val TEST_INSTANT: Instant = Instant.parse("2026-08-25T12:00:00Z")
        }
    }
}
