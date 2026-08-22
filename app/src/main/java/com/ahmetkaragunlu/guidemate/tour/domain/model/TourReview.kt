package com.ahmetkaragunlu.guidemate.tour.domain.model

import androidx.annotation.DrawableRes
import java.time.Instant

data class TourReview(
    val id: String,
    val reviewerName: String,
    val rating: Int,
    val comment: String,
    @param:DrawableRes val reviewerImageResId: Int,
    val reviewerImageUrl: String? = null,
    val submittedAt: Instant? = null,
)
