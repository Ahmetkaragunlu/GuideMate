package com.ahmetkaragunlu.guidemate.review.presentation.model

import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailStatus

enum class TourReviewAvailability {
    UNAVAILABLE,
    AVAILABLE,
    SUBMITTED,
}

internal fun resolveTourReviewAvailability(
    reservation: TouristReservation?,
    detailStatus: TourDetailStatus?,
): TourReviewAvailability =
    when {
        reservation == null -> TourReviewAvailability.UNAVAILABLE
        reservation.status != TouristReservationStatus.CONFIRMED ->
            TourReviewAvailability.UNAVAILABLE
        detailStatus != TourDetailStatus.COMPLETED -> TourReviewAvailability.UNAVAILABLE
        reservation.review != null -> TourReviewAvailability.SUBMITTED
        else -> TourReviewAvailability.AVAILABLE
    }
