package com.ahmetkaragunlu.guidemate.screens.guide.tours.model

import java.time.Instant

data class CreateTourSessionRequest(
    val tourId: String,
    val meetingPoint: String,
    val startsAt: Instant,
    val durationMinutes: Int,
    val price: Double,
    val capacity: Int,
)
