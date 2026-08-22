package com.ahmetkaragunlu.guidemate.review.domain.model

import java.time.Instant

data class ReviewSubmissionInput(
    val rating: Int,
    val comment: String,
)

data class SubmittedReview(
    val id: String,
    val rating: Int,
    val comment: String,
    val submittedAt: Instant,
)
