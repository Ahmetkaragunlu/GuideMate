package com.ahmetkaragunlu.guidemate.review.domain.repository

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.review.domain.model.ReviewSubmissionInput
import com.ahmetkaragunlu.guidemate.review.domain.model.SubmittedReview
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourReview
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    val reviewChanges: Flow<Unit>

    suspend fun submitReview(
        reservationId: String,
        input: ReviewSubmissionInput,
    ): DataResult<SubmittedReview>

    suspend fun getTourReviews(
        tourId: String,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourReview>>
}
