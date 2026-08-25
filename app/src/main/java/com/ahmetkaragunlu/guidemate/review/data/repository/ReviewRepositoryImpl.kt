package com.ahmetkaragunlu.guidemate.review.data.repository

import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.review.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.review.data.mapper.toDto
import com.ahmetkaragunlu.guidemate.review.data.remote.api.ReviewApi
import com.ahmetkaragunlu.guidemate.review.domain.model.ReviewSubmissionInput
import com.ahmetkaragunlu.guidemate.review.domain.model.SubmittedReview
import com.ahmetkaragunlu.guidemate.review.domain.repository.ReviewRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourReview
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ReviewRepositoryImpl @Inject constructor(
    private val api: ReviewApi,
    private val apiCallExecutor: ApiCallExecutor,
) : ReviewRepository {
    private val mutableReviewChanges = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val reviewChanges: Flow<Unit> = mutableReviewChanges.asSharedFlow()

    override suspend fun submitReview(
        reservationId: String,
        input: ReviewSubmissionInput,
    ): DataResult<SubmittedReview> =
        apiCallExecutor.execute(
            request = { api.submitReview(reservationId, input.toDto()) },
            transform = { it.toDomain() },
        ).also { result ->
            if (result is DataResult.Success) mutableReviewChanges.tryEmit(Unit)
        }

    override suspend fun getTourReviews(
        tourId: String,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourReview>> =
        apiCallExecutor.execute(
            request = { api.getTourReviews(tourId = tourId, page = page, size = size) },
            transform = { it.toDomain() },
        )
}
