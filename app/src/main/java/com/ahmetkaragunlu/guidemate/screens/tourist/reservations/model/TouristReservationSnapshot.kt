package com.ahmetkaragunlu.guidemate.screens.tourist.reservations.model

import androidx.annotation.DrawableRes
import com.ahmetkaragunlu.guidemate.screens.common.guide.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategory
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.TourLanguage
import java.time.Instant

data class TouristReservationSnapshot(
    val tourId: String,
    val guide: GuidePublicSummary,
    val title: String,
    val description: String,
    val country: String,
    val city: String,
    val timeZoneId: String,
    val category: TourCategory,
    val languages: List<TourLanguage>,
    @param:DrawableRes val coverImageResId: Int,
    val coverImageUrl: String?,
    val startsAt: Instant,
    val durationMinutes: Int,
    val meetingPoint: String,
    val unitPriceMinor: Long,
) {
    val endsAt: Instant
        get() = startsAt.plusSeconds(durationMinutes * 60L)
}
