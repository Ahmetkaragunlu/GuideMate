package com.ahmetkaragunlu.guidemate.review.data.mapper

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.review.data.remote.model.ReviewSubmissionRequestDto
import com.ahmetkaragunlu.guidemate.review.data.remote.model.SubmittedReviewResponseDto
import com.ahmetkaragunlu.guidemate.review.data.remote.model.TourReviewResponseDto
import com.ahmetkaragunlu.guidemate.review.domain.model.ReviewSubmissionInput
import com.ahmetkaragunlu.guidemate.review.domain.model.SubmittedReview
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourReview
import java.time.Instant

fun ApiPageResponse<TourReviewResponseDto>.toDomain(): PagedResult<TourReview> =
    PagedResult(
        items = content.map(TourReviewResponseDto::toDomain),
        page = page,
        size = size,
        totalElements = totalElements,
        totalPages = totalPages,
        isFirst = isFirst,
        isLast = isLast,
    )

private fun TourReviewResponseDto.toDomain(): TourReview =
    TourReview(
        id = reviewId,
        reviewerName = reviewerDisplayName,
        rating = rating,
        comment = comment.orEmpty(),
        reviewerImageResId = R.drawable.unnamed,
        reviewerImageUrl = reviewerAvatar?.imageUrl,
        submittedAt = Instant.parse(submittedAt),
    )

fun ReviewSubmissionInput.toDto(): ReviewSubmissionRequestDto =
    ReviewSubmissionRequestDto(
        rating = rating,
        comment = comment.trim().takeIf(String::isNotEmpty),
    )

fun SubmittedReviewResponseDto.toDomain(): SubmittedReview =
    SubmittedReview(
        id = reviewId,
        rating = rating,
        comment = comment.orEmpty(),
        submittedAt = Instant.parse(submittedAt),
    )
