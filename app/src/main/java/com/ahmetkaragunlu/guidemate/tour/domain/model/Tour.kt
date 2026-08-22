package com.ahmetkaragunlu.guidemate.tour.domain.model

import androidx.annotation.DrawableRes
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import java.time.Instant

data class Tour(
    val id: String,
    val version: Long = 0,
    val guide: GuidePublicSummary,
    val title: String,
    val description: String,
    val countryCode: String = "",
    val country: String,
    val cityPlaceId: String = "",
    val city: String,
    val timeZoneId: String,
    val category: TourCategory,
    val languages: List<TourLanguage>,
    @param:DrawableRes val coverImageResId: Int,
    val coverMediaId: String? = null,
    val coverImageUrl: String? = null,
    val approvalStatus: TourApprovalStatus,
    val approvalSubmittedAt: Instant? = null,
    val publishedAt: Instant? = null,
    val rejectionReason: String? = null,
    val averageRating: Double? = null,
    val reviewCount: Long = 0,
    val recentReviews: List<TourReview> = emptyList(),
)
