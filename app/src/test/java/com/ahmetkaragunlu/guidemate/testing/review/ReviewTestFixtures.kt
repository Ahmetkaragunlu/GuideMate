package com.ahmetkaragunlu.guidemate.testing.review

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.review.domain.model.ReviewSubmissionInput
import com.ahmetkaragunlu.guidemate.review.domain.model.SubmittedReview
import com.ahmetkaragunlu.guidemate.review.domain.repository.ReviewRepository
import com.ahmetkaragunlu.guidemate.testing.common.emptyPage
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourReview
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

data class ReviewSubmissionCall(
    val reservationId: String,
    val input: ReviewSubmissionInput,
)

class FakeReviewRepository : ReviewRepository {
    val reviewChangeEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val reviewChanges: Flow<Unit> = reviewChangeEvents
    var submitResult: DataResult<SubmittedReview> = DataResult.Success(testSubmittedReview())
    var reviewsResult: DataResult<PagedResult<TourReview>> = DataResult.Success(emptyPage())
    val reviewsResults = ArrayDeque<DataResult<PagedResult<TourReview>>>()
    val tourReviewRequests = mutableListOf<String>()
    var submittedReview: ReviewSubmissionCall? = null
    var ownedTourReviewsRequested = false

    fun publishReviewChange() {
        check(reviewChangeEvents.tryEmit(Unit))
    }

    override suspend fun submitReview(
        reservationId: String,
        input: ReviewSubmissionInput,
    ): DataResult<SubmittedReview> {
        submittedReview = ReviewSubmissionCall(reservationId = reservationId, input = input)
        return submitResult
    }

    override suspend fun getTourReviews(
        tourId: String,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourReview>> {
        tourReviewRequests += tourId
        return reviewsResults.removeFirstOrNull() ?: reviewsResult
    }

    override suspend fun getOwnedTourReviews(
        tourId: String,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourReview>> {
        ownedTourReviewsRequested = true
        return reviewsResult
    }
}

fun testSubmittedReview(): SubmittedReview =
    SubmittedReview(
        id = "review-1",
        rating = 5,
        comment = "Excellent tour",
        submittedAt = Instant.parse("2026-01-01T00:00:00Z"),
    )
