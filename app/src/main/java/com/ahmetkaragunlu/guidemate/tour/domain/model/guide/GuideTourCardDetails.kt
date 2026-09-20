package com.ahmetkaragunlu.guidemate.tour.domain.model.guide

import java.time.Instant

data class GuideTourCardSchedule(
    val cityName: String,
    val countryCode: String,
    val timeZoneId: String,
    val startsAt: Instant,
    val durationMinutes: Int,
)

data class GuideTourCardPricing(
    val priceMinor: Long,
    val currencyCode: String,
    val netEarningsMinor: Long?,
)

data class GuideTourCardStats(
    val bookedCount: Int,
    val capacity: Int,
    val averageRating: Double,
    val reviewCount: Long,
)
