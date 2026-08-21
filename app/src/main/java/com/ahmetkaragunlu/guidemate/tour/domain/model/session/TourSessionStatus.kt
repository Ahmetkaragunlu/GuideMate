package com.ahmetkaragunlu.guidemate.tour.domain.model.session

enum class TourSessionStatus(
    val canToggleBookingAvailability: Boolean,
    val isTerminal: Boolean,
) {
    OPEN_FOR_BOOKING(canToggleBookingAvailability = true, isTerminal = false),
    CLOSED(canToggleBookingAvailability = true, isTerminal = false),
    COMPLETED(canToggleBookingAvailability = false, isTerminal = true),
    CANCELLED(canToggleBookingAvailability = false, isTerminal = true),
}
