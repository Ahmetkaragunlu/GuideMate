package com.ahmetkaragunlu.guidemate.reservation.domain.model

enum class ReservationListType {
    UPCOMING,
    PAST,
}

data class CancelReservationInput(
    val version: Long,
    val reason: String? = null,
)
