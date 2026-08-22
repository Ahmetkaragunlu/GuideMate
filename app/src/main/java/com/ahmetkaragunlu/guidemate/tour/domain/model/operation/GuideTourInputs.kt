package com.ahmetkaragunlu.guidemate.tour.domain.model.operation

import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import java.time.Instant

data class TourContentInput(
    val title: String,
    val description: String,
    val countryCode: String,
    val cityPlaceId: String,
    val cityName: String,
    val timeZoneId: String,
    val category: TourCategory,
    val languageCodes: List<String>,
    val coverMediaId: String,
)

data class TourSessionInput(
    val meetingPoint: String,
    val startsAt: Instant,
    val durationMinutes: Int,
    val priceMinor: Long,
    val capacity: Int,
)

data class CreateGuideTourInput(
    val content: TourContentInput,
    val session: TourSessionInput,
)

data class SubmitTourChangeInput(
    val baseVersion: Long,
    val content: TourContentInput,
)

data class UpdateTourSessionInput(
    val version: Long,
    val session: TourSessionInput,
)
