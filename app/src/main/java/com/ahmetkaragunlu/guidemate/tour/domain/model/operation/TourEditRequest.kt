package com.ahmetkaragunlu.guidemate.tour.domain.model.operation

import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import java.time.Instant

data class TourEditRequest(
    val title: String,
    val description: String,
    val country: String,
    val city: String,
    val category: TourCategory,
    val languages: List<TourLanguage>,
    val selectedCoverImageUri: String?,
    val meetingPoint: String,
    val startsAt: Instant,
    val durationMinutes: Int,
    val priceMinor: Long,
    val capacity: Int,
)
