package com.ahmetkaragunlu.guidemate.screens.tourist.reviews.model

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailStatus
import com.ahmetkaragunlu.guidemate.screens.tourist.reservations.model.TouristReservation
import com.ahmetkaragunlu.guidemate.screens.tourist.reservations.model.TouristReservationStatus

data class TourReviewFormUiState(
    val isVisible: Boolean = false,
    val rating: Int = 0,
    val comment: String = "",
    val isSubmitting: Boolean = false,
    @param:StringRes val errorResId: Int? = null,
    val showSuccessDialog: Boolean = false,
)

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
