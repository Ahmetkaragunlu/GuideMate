package com.ahmetkaragunlu.guidemate.reservation.domain.model

import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage

data class TouristReservationSnapshot(
    val tourId: String,
    val guide: GuidePublicSummary,
    val title: String,
    val description: String,
    val location: ReservationLocationSnapshot,
    val category: TourCategory,
    val languages: List<TourLanguage>,
    val cover: MediaReference?,
    val schedule: ReservationScheduleSnapshot,
)
