package com.ahmetkaragunlu.guidemate.tour.domain.model

import java.time.Instant

data class TourLocation(
    val countryCode: String = "",
    val country: String,
    val cityPlaceId: String = "",
    val city: String,
    val timeZoneId: String,
)

data class TourPublication(
    val approvalStatus: TourApprovalStatus,
    val approvalSubmittedAt: Instant? = null,
    val publishedAt: Instant? = null,
    val rejectionReason: String? = null,
)

data class TourReviews(
    val averageRating: Double? = null,
    val reviewCount: Long = 0,
    val recentReviews: List<TourReview> = emptyList(),
)
