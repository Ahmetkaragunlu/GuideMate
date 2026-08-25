package com.ahmetkaragunlu.guidemate.tour.domain.model

import java.time.Instant

data class TourReview(
    val id: String,
    val reviewerName: String,
    val rating: Int,
    val comment: String,
    val reviewerImageUrl: String? = null,
    val submittedAt: Instant? = null,
)
