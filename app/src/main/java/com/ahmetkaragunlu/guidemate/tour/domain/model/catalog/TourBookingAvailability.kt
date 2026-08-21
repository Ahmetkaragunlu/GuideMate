package com.ahmetkaragunlu.guidemate.tour.domain.model.catalog

import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.effectiveStatus
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

    val effectiveStatus = session.effectiveStatus(now)
    return when {
        effectiveStatus == TourSessionStatus.CANCELLED -> TourBookingAvailability.CANCELLED
        effectiveStatus == TourSessionStatus.COMPLETED -> TourBookingAvailability.COMPLETED
        hasReservation -> TourBookingAvailability.ALREADY_RESERVED
        !session.startsAt.isAfter(now) -> TourBookingAvailability.STARTED
        tour.approvalStatus != TourApprovalStatus.APPROVED ->
            TourBookingAvailability.NOT_APPROVED
        session.status != TourSessionStatus.OPEN_FOR_BOOKING -> TourBookingAvailability.CLOSED
        session.bookedCount >= session.capacity -> TourBookingAvailability.FULL
        else -> TourBookingAvailability.AVAILABLE
    }
}
