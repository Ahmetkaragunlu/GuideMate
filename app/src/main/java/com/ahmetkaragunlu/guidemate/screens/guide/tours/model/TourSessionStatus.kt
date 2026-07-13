package com.ahmetkaragunlu.guidemate.screens.guide.tours.model

enum class TourSessionStatus(
    val isBookingManageable: Boolean,
    val isTerminal: Boolean,
) {
    OPEN_FOR_BOOKING(isBookingManageable = true, isTerminal = false),
    CLOSED(isBookingManageable = true, isTerminal = false),
    COMPLETED(isBookingManageable = false, isTerminal = true),
    CANCELLED(isBookingManageable = false, isTerminal = true),
}
