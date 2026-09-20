package com.ahmetkaragunlu.guidemate.reservation.domain.model

import com.ahmetkaragunlu.guidemate.review.domain.model.SubmittedReview
import java.time.Instant

data class TouristReservation(
    val id: String,
    val tourSessionId: String,
    val version: Long,
    val purchase: ReservationPurchaseDetails,
    val snapshot: TouristReservationSnapshot,
    val status: TouristReservationStatus,
    val holdExpiresAt: Instant? = null,
    val cancellation: ReservationCancellationDetails,
    val rating: ReservationRatingSummary,
    val attendance: ReservationAttendance,
    val review: SubmittedReview? = null,
)
