package com.ahmetkaragunlu.guidemate.tour.domain.model.guide

import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import java.time.Instant

data class GuideTourCard(
    val tourId: String,
    val sessionId: String,
    val tourVersion: Long,
    val sessionVersion: Long,
    val title: String,
    val cityName: String,
    val countryCode: String,
    val timeZoneId: String,
    val category: TourCategory,
    val languageCodes: List<String>,
    val cover: MediaReference?,
    val startsAt: Instant,
    val durationMinutes: Int,
    val priceMinor: Long,
    val currencyCode: String,
    val bookedCount: Int,
    val capacity: Int,
    val averageRating: Double,
    val reviewCount: Long,
    val netEarningsMinor: Long?,
    val approvalStatus: TourApprovalStatus,
    val sessionStatus: TourSessionStatus,
    val rejectionReason: String?,
    val canArchive: Boolean,
)
