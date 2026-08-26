package com.ahmetkaragunlu.guidemate.testing

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.review.domain.model.ReviewSubmissionInput
import com.ahmetkaragunlu.guidemate.review.domain.model.SubmittedReview
import com.ahmetkaragunlu.guidemate.review.domain.repository.ReviewRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourReview
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeReviewRepository : ReviewRepository {
    override val reviewChanges: Flow<Unit> = MutableSharedFlow()
    var submitResult: DataResult<SubmittedReview> = DataResult.Success(testSubmittedReview())
    var reviewsResult: DataResult<PagedResult<TourReview>> = DataResult.Success(emptyPage())
    var submittedReview: Pair<String, ReviewSubmissionInput>? = null

    override suspend fun submitReview(
        reservationId: String,
        input: ReviewSubmissionInput,
    ): DataResult<SubmittedReview> {
        submittedReview = reservationId to input
        return submitResult
    }

    override suspend fun getTourReviews(
        tourId: String,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourReview>> = reviewsResult
}

fun testSubmittedReview(): SubmittedReview =
    SubmittedReview(
        id = "review-1",
        rating = 5,
        comment = "Excellent tour",
        submittedAt = Instant.parse("2026-01-01T00:00:00Z"),
    )
