package com.ahmetkaragunlu.guidemate.screens.guide.tours.model

import androidx.annotation.DrawableRes
import java.time.Instant

data class Tour(
    val id: String,
    val guideId: String,
    val title: String,
    val description: String,
    val country: String,
    val city: String,
    val timeZoneId: String,
    val category: String,
    val languages: List<TourLanguage>,
    @param:DrawableRes val coverImageResId: Int,
    val coverImageUrl: String? = null,
    val approvalStatus: TourApprovalStatus,
    val reviewSubmittedAt: Instant? = null,
    val publishedAt: Instant? = null,
    val rejectionReason: String? = null,
    val averageRating: Double? = null,
    val reviewCount: Int = 0,
    val recentReviews: List<TourReview> = emptyList(),
)
