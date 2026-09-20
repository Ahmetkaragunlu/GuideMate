package com.ahmetkaragunlu.guidemate.reservation.presentation.mapper

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationActor
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model.TripUiModel
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourReview
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailGuideUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailReviewUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailSessionUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailStatus
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailTourUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.formatting.formatTourDateTime

fun TouristReservation.toTripUiModel(): TripUiModel {
    val detail = toTourDetailUiState()
    return TripUiModel(
        reservationId = id,
        tourSessionId = tourSessionId,
        title = detail.tour.title,
        date = detail.session.date,
        location = detail.tour.location,
        imageResId = detail.tour.imageResId,
        imageUrl = detail.tour.imageUrl,
        participantCount = purchase.participantCount,
        category = checkNotNull(detail.tour.category),
        languagesFlag = detail.tour.languagesFlag,
        languagesText = detail.tour.languagesText,
        totalPriceMinor = purchase.totalPriceMinor,
        startsAt = snapshot.schedule.startsAt,
        sessionStatus = detail.session.status,
        cancellationTitleResId = cancellationTitleResId(),
        cancellationReason = detail.session.cancellationReason,
    )
}

fun TouristReservation.toTourDetailUiState(
    publicReviews: List<TourReview> = emptyList(),
): TourDetailUiState =
    TourDetailUiState(
        tour =
            TourDetailTourUiState(
                id = snapshot.tourId,
                title = snapshot.title,
                imageResId = R.drawable.ic_image_unavailable,
                imageUrl = snapshot.cover?.imageUrl,
                rating = rating.averageRating.takeIf { rating.reviewCount > 0 },
                reviewCount = rating.reviewCount,
                location =
                    listOf(snapshot.location.city, snapshot.location.country)
                        .filter(String::isNotBlank)
                        .joinToString(", "),
                languagesFlag =
                    snapshot.languages.joinToString(separator = " ") { it.flagEmoji },
                languagesText =
                    snapshot.languages.joinToString(separator = ", ") { it.shortCode },
                category = snapshot.category,
                description = snapshot.description,
            ),
        session =
            TourDetailSessionUiState(
                sessionId = tourSessionId,
                date = snapshot.schedule.startsAt.formatTourDateTime(snapshot.location.timeZoneId),
                durationMinutes = snapshot.schedule.durationMinutes,
                priceMinor = purchase.unitPriceMinor,
                bookedCount = attendance.bookedCount,
                capacity = attendance.capacity,
                meetingPoint = snapshot.schedule.meetingPoint,
                status = status.toDetailStatus(),
                cancellationReason = cancellation.reason,
            ),
        guide =
            TourDetailGuideUiState(
                id = snapshot.guide.id,
                name = snapshot.guide.displayName,
                imageResId = R.drawable.ic_default_avatar,
                imageUrl = snapshot.guide.profileImageUrl,
            ),
        reviews = publicReviews.map(TourReview::toDetailReviewUiModel),
    )

private fun TouristReservationStatus.toDetailStatus(): TourDetailStatus? =
    when (this) {
        TouristReservationStatus.COMPLETED -> TourDetailStatus.COMPLETED
        TouristReservationStatus.CANCELLED -> TourDetailStatus.CANCELLED
        TouristReservationStatus.PENDING_PAYMENT,
        TouristReservationStatus.CONFIRMED,
        TouristReservationStatus.EXPIRED,
        -> null
    }

private fun TouristReservation.cancellationTitleResId(): Int? {
    if (status != TouristReservationStatus.CANCELLED) return null
    return if (cancellation.actor == ReservationCancellationActor.TOURIST) {
        R.string.reservation_cancelled_success
    } else {
        R.string.tour_cancelled_status
    }
}

private fun TourReview.toDetailReviewUiModel(): TourDetailReviewUiModel =
    TourDetailReviewUiModel(
        id = id,
        reviewerName = reviewerName,
        comment = comment,
        rating = rating,
        reviewerImageResId = R.drawable.ic_default_avatar,
        reviewerImageUrl = reviewerImageUrl,
    )
