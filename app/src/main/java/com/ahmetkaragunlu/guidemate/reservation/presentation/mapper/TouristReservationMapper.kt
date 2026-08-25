package com.ahmetkaragunlu.guidemate.reservation.presentation.mapper

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model.TripUiModel
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourReview
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailReviewUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailStatus
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.formatting.formatTourDateTime

fun TouristReservation.toTripUiModel(): TripUiModel {
    val detail = toTourDetailUiState()
    return TripUiModel(
        reservationId = id,
        tourSessionId = tourSessionId,
        title = detail.title,
        date = detail.date,
        location = detail.location,
        imageResId = detail.imageResId,
        imageUrl = detail.imageUrl,
        participantCount = participantCount,
        category = checkNotNull(detail.category),
        languagesFlag = detail.languagesFlag,
        languagesText = detail.languagesText,
        totalPriceMinor = totalPriceMinor,
        startsAt = snapshot.startsAt,
        sessionStatus = detail.sessionStatus,
        cancellationReason = detail.cancellationReason,
    )
}

fun TouristReservation.toTourDetailUiState(
    publicReviews: List<TourReview> = emptyList(),
    publicReviewCount: Long = 0,
): TourDetailUiState =
    TourDetailUiState(
        sessionId = tourSessionId,
        tourId = snapshot.tourId,
        title = snapshot.title,
        imageResId = R.drawable.example,
        imageUrl = snapshot.coverImageUrl,
        reviewCount = publicReviewCount,
        date = snapshot.startsAt.formatTourDateTime(snapshot.timeZoneId),
        durationMinutes = snapshot.durationMinutes,
        location =
            listOf(snapshot.city, snapshot.country)
                .filter(String::isNotBlank)
                .joinToString(", "),
        languagesFlag = snapshot.languages.joinToString(separator = " ") { it.flagEmoji },
        languagesText = snapshot.languages.joinToString(separator = ", ") { it.shortCode },
        category = snapshot.category,
        priceMinor = unitPriceMinor,
        reservedParticipantCount = participantCount,
        description = snapshot.description,
        meetingPoint = snapshot.meetingPoint,
        sessionStatus = status.toDetailStatus(),
        cancellationReason = cancellationReason,
        guideId = snapshot.guide.id,
        guideName = snapshot.guide.displayName,
        guideImageResId = R.drawable.unnamed,
        guideImageUrl = snapshot.guide.profileImageUrl,
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

private fun TourReview.toDetailReviewUiModel(): TourDetailReviewUiModel =
    TourDetailReviewUiModel(
        id = id,
        reviewerName = reviewerName,
        comment = comment,
        rating = rating,
        reviewerImageResId = R.drawable.unnamed,
        reviewerImageUrl = reviewerImageUrl,
    )
