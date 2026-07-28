package com.ahmetkaragunlu.guidemate.screens.common.tours.model.session

import java.time.Instant

data class TourSession(
    val id: String,
    val tourId: String,
    val meetingPoint: String,
    val startsAt: Instant,
    val durationMinutes: Int,
    val priceMinor: Long,
    val capacity: Int,
    val bookedCount: Int,
    val status: TourSessionStatus,
    val earningsMinor: Long? = null,
    val cancellationReason: String? = null,
) {
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
