package com.ahmetkaragunlu.guidemate.review.data.mapper

import com.ahmetkaragunlu.guidemate.common.network.model.response.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.review.data.remote.model.request.ReviewSubmissionRequest
import com.ahmetkaragunlu.guidemate.review.data.remote.model.response.SubmittedReviewResponse
import com.ahmetkaragunlu.guidemate.review.data.remote.model.response.TourReviewResponse
import com.ahmetkaragunlu.guidemate.review.domain.model.ReviewSubmissionInput
import com.ahmetkaragunlu.guidemate.review.domain.model.SubmittedReview
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourReview
import java.time.Instant

fun ApiPageResponse<TourReviewResponse>.toDomain(): PagedResult<TourReview> =
    PagedResult(
        items = content.map(TourReviewResponse::toDomain),
        page = page,
        size = size,
        totalElements = totalElements,
        totalPages = totalPages,
        isFirst = isFirst,
        isLast = isLast,
    )

private fun TourReviewResponse.toDomain(): TourReview =
    TourReview(
        id = reviewId,
        reviewerName = reviewerDisplayName,
        rating = rating,
        comment = comment.orEmpty(),
        reviewerImageUrl = reviewerAvatar?.imageUrl,
        submittedAt = Instant.parse(submittedAt),
    )

fun ReviewSubmissionInput.toDto(): ReviewSubmissionRequest =
    ReviewSubmissionRequest(
        rating = rating,
        comment = comment.trim().takeIf(String::isNotEmpty),
    )

fun SubmittedReviewResponse.toDomain(): SubmittedReview =
    SubmittedReview(
        id = reviewId,
        rating = rating,
        comment = comment.orEmpty(),
        submittedAt = Instant.parse(submittedAt),
    )
