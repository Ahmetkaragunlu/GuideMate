package com.ahmetkaragunlu.guidemate.tour.domain.model.discovery

import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import java.time.Instant

data class TourSearchItem(
    val tourId: String,
    val sessionId: String,
    val title: String,
    val category: TourCategory,
    val cityName: String,
    val countryCode: String,
    val cityPlaceId: String,
    val startsAt: Instant,
    val timeZoneId: String,
    val durationMinutes: Int,
    val priceMinor: Long,
    val currencyCode: String,
    val availableCapacity: Int,
    val languageCodes: List<String>,
    val cover: MediaReference,
    val averageRating: Double?,
    val reviewCount: Long,
    val guide: GuidePublicSummary,
)
