package com.ahmetkaragunlu.guidemate.reservation.domain.model

import java.time.Instant

data class ReservationPurchaseDetails(
    val participantCount: Int,
    val unitPriceMinor: Long,
    val totalPriceMinor: Long,
    val currencyCode: String,
)

data class ReservationCancellationDetails(
    val actor: ReservationCancellationActor?,
    val reason: String?,
    val cancelledAt: Instant?,
    val refundEligibility: ReservationRefundEligibility,
    val policy: ReservationCancellationPolicy,
)

data class ReservationCancellationPolicy(
    val code: String,
    val version: Int,
)

data class ReservationRatingSummary(
    val averageRating: Double,
    val reviewCount: Long,
)

data class ReservationAttendance(
    val bookedCount: Int,
    val capacity: Int,
)

data class ReservationLocationSnapshot(
    val countryCode: String,
    val country: String,
    val cityPlaceId: String,
    val city: String,
    val timeZoneId: String,
)

data class ReservationScheduleSnapshot(
    val startsAt: Instant,
    val durationMinutes: Int,
    val meetingPoint: String,
) {
    val endsAt: Instant
        get() = startsAt.plusSeconds(durationMinutes * 60L)
}
