package com.ahmetkaragunlu.guidemate.reservation.domain.model

data class TouristReservation(
    val id: String,
    val tourSessionId: String,
    val participantCount: Int,
    val snapshot: TouristReservationSnapshot,
    val status: TouristReservationStatus,
    val review: ReservationReview? = null,
)
