package com.ahmetkaragunlu.guidemate.screens.tourist.reservations.model

data class TouristReservation(
    val id: String,
    val tourSessionId: String,
    val participantCount: Int,
    val snapshot: TouristReservationSnapshot,
    val status: TouristReservationStatus,
    val review: ReservationReview? = null,
)
