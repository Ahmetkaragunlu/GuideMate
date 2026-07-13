package com.ahmetkaragunlu.guidemate.screens.guide.tours.model

import java.time.Instant

data class TourSession(
    val id: String,
    val tourId: String,
    val meetingPoint: String,
    val startsAt: Instant,
    val durationMinutes: Int,
    val price: Double,
    val capacity: Int,
    val bookedCount: Int,
    val status: TourSessionStatus,
    val earnings: Double? = null,
    val cancellationReason: String? = null,
) {
    val endsAt: Instant
        get() = startsAt.plusSeconds(durationMinutes * 60L)
}
