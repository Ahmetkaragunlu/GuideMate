package com.ahmetkaragunlu.guidemate.reservation.domain.model

import androidx.annotation.DrawableRes
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import java.time.Instant

data class TouristReservationSnapshot(
    val tourId: String,
    val guide: GuidePublicSummary,
    val title: String,
    val description: String,
    val countryCode: String,
    val country: String,
    val cityPlaceId: String,
    val city: String,
    val timeZoneId: String,
    val category: TourCategory,
    val languages: List<TourLanguage>,
    @param:DrawableRes val coverImageResId: Int,
    val coverMediaId: String?,
    val coverImageUrl: String?,
    val startsAt: Instant,
    val durationMinutes: Int,
    val meetingPoint: String,
    val unitPriceMinor: Long,
) {
    val endsAt: Instant
        get() = startsAt.plusSeconds(durationMinutes * 60L)
}
