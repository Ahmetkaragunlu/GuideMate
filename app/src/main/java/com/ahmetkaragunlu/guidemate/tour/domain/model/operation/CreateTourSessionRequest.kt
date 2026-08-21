package com.ahmetkaragunlu.guidemate.tour.domain.model.operation

import java.time.Instant

data class CreateTourSessionRequest(
    val tourId: String,
    val meetingPoint: String,
    val startsAt: Instant,
    val durationMinutes: Int,
    val priceMinor: Long,
    val capacity: Int,
)
