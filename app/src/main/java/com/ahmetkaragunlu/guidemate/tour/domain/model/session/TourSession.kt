package com.ahmetkaragunlu.guidemate.tour.domain.model.session

import java.time.Instant

data class TourSession(
    val id: String,
    val tourId: String,
    val version: Long = 0,
    val meetingPoint: String,
    val startsAt: Instant,
    val durationMinutes: Int,
    val priceMinor: Long,
    val currencyCode: String = "USD",
    val capacity: Int,
    val bookedCount: Int,
    val status: TourSessionStatus,
    val earningsMinor: Long? = null,
    val cancellationActor: TourCancellationActor? = null,
    val cancellationReason: String? = null,
    val cancelledAt: Instant? = null,
) {
    val availableCapacity: Int
        get() = (capacity - bookedCount).coerceAtLeast(0)

    val endsAt: Instant
        get() = startsAt.plusSeconds(durationMinutes * 60L)
}

fun TourSession.effectiveStatus(now: Instant): TourSessionStatus =
    when {
        status == TourSessionStatus.CANCELLED -> TourSessionStatus.CANCELLED
        status == TourSessionStatus.COMPLETED || !endsAt.isAfter(now) ->
            TourSessionStatus.COMPLETED
        else -> status
    }

fun TourSession.isEffectivelyTerminal(now: Instant): Boolean = effectiveStatus(now).isTerminal
