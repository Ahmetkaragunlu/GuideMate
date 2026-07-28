package com.ahmetkaragunlu.guidemate.screens.tourist.reservations.model

import java.time.Instant

data class ReservationReview(
    val id: String,
    val rating: Int,
    val comment: String,
    val submittedAt: Instant,
)

data class CreateTourReviewRequest(
    val reservationId: String,
    val rating: Int,
    val comment: String,
)
