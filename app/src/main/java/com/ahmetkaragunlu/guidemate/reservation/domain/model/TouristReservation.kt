package com.ahmetkaragunlu.guidemate.reservation.domain.model

import com.ahmetkaragunlu.guidemate.review.domain.model.SubmittedReview
import java.time.Instant

data class TouristReservation(
    val id: String,
    val tourSessionId: String,
    val version: Long,
    val participantCount: Int,
    val unitPriceMinor: Long,
    val totalPriceMinor: Long,
    val currencyCode: String,
    val snapshot: TouristReservationSnapshot,
    val status: TouristReservationStatus,
    val holdExpiresAt: Instant? = null,
    val cancellationActor: ReservationCancellationActor? = null,
    val cancellationReason: String? = null,
    val cancelledAt: Instant? = null,
    val refundEligibility: ReservationRefundEligibility = ReservationRefundEligibility.NOT_APPLICABLE,
    val cancellationPolicyCode: String,
    val cancellationPolicyVersion: Int,
    val review: SubmittedReview? = null,
)
