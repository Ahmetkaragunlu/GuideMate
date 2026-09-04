package com.ahmetkaragunlu.guidemate.tour.domain.model.catalog

import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import java.time.Instant

enum class TourBookingAvailability {
    AVAILABLE,
    ALREADY_RESERVED,
    NOT_APPROVED,
    CLOSED,
    FULL,
    STARTED,
    COMPLETED,
    CANCELLED,
    EXPIRED,
    UNAVAILABLE,
    ;

    val isBookable: Boolean
        get() = this == AVAILABLE
}

fun TourWithSession?.resolveBookingAvailability(
    hasReservation: Boolean,
    now: Instant = Instant.now(),
): TourBookingAvailability {
    if (this == null) {
        return if (hasReservation) {
            TourBookingAvailability.ALREADY_RESERVED
        } else {
            TourBookingAvailability.UNAVAILABLE
        }
    }

    return when {
        session.status == TourSessionStatus.CANCELLED -> TourBookingAvailability.CANCELLED
        session.status == TourSessionStatus.COMPLETED -> TourBookingAvailability.COMPLETED
        session.status == TourSessionStatus.EXPIRED -> TourBookingAvailability.EXPIRED
        hasReservation -> TourBookingAvailability.ALREADY_RESERVED
        !session.startsAt.isAfter(now) -> TourBookingAvailability.STARTED
        tour.approvalStatus != TourApprovalStatus.APPROVED ->
            TourBookingAvailability.NOT_APPROVED
        session.status != TourSessionStatus.OPEN_FOR_BOOKING -> TourBookingAvailability.CLOSED
        session.bookedCount >= session.capacity -> TourBookingAvailability.FULL
        else -> TourBookingAvailability.AVAILABLE
    }
}
