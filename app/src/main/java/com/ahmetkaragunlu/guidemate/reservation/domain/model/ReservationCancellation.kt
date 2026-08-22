package com.ahmetkaragunlu.guidemate.reservation.domain.model

enum class ReservationCancellationActor {
    TOURIST,
    GUIDE,
    ADMIN,
    SYSTEM,
}

enum class ReservationRefundEligibility {
    FULL_REFUND,
    NO_REFUND,
    NOT_APPLICABLE,
}

enum class ReservationRefundStatus {
    REQUESTED,
    PROCESSING,
    SUCCEEDED,
    FAILED,
    MANUAL_REVIEW,
}

data class ReservationCancellationResult(
    val reservation: TouristReservation,
    val refundEligibility: ReservationRefundEligibility,
    val refundId: String?,
    val refundStatus: ReservationRefundStatus?,
)
