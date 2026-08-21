package com.ahmetkaragunlu.guidemate.reservation.presentation.mapper

import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailReviewUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailStatus
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.formatting.formatTourDateTime
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model.TripUiModel
import java.time.Instant

fun TouristReservation.toTripUiModel(
    currentTour: TourWithSession?,
    now: Instant = Instant.now(),
): TripUiModel {
    val detail = toTourDetailUiState(currentTour = currentTour, now = now)
    return TripUiModel(
        id = id,
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
        priceMinor = detail.priceMinor,
        rating = detail.rating,
        reviewCount = detail.reviewCount.takeIf { it > 0 },
        startsAt = snapshot.startsAt,
        sessionStatus = detail.sessionStatus,
        cancellationReason = detail.cancellationReason,
    )
}

fun TouristReservation.toTourDetailUiState(
    currentTour: TourWithSession?,
    now: Instant = Instant.now(),
): TourDetailUiState {
    val currentSession = currentTour?.session
    val currentTourData = currentTour?.tour
    val detailStatus =
        when {
            status == TouristReservationStatus.CANCELLED -> TourDetailStatus.CANCELLED
            currentSession?.status == TourSessionStatus.CANCELLED -> TourDetailStatus.CANCELLED
            currentSession?.status == TourSessionStatus.COMPLETED ||
                !snapshot.endsAt.isAfter(now) ->
                TourDetailStatus.COMPLETED
            else -> null
        }

    return TourDetailUiState(
        sessionId = tourSessionId,
        tourId = snapshot.tourId,
        title = snapshot.title,
        imageResId = snapshot.coverImageResId,
        imageUrl = snapshot.coverImageUrl,
        rating = currentTourData?.averageRating,
        reviewCount = currentTourData?.reviewCount ?: 0,
        date = snapshot.startsAt.formatTourDateTime(snapshot.timeZoneId),
        durationMinutes = snapshot.durationMinutes,
        location =
            listOf(snapshot.city, snapshot.country)
                .filter(String::isNotBlank)
                .joinToString(", "),
        languagesFlag = snapshot.languages.joinToString(separator = " ") { it.flagEmoji },
        languagesText = snapshot.languages.joinToString(separator = ", ") { it.shortCode },
        category = snapshot.category,
        priceMinor = snapshot.unitPriceMinor,
        bookedCount = currentSession?.bookedCount ?: participantCount,
        capacity = currentSession?.capacity ?: participantCount,
        description = snapshot.description,
        meetingPoint = snapshot.meetingPoint,
        sessionStatus = detailStatus,
        cancellationReason = currentSession?.cancellationReason,
        guideId = snapshot.guide.id,
        guideName = snapshot.guide.displayName,
        guideImageResId = snapshot.guide.profileImageResId,
        guideImageUrl = snapshot.guide.profileImageUrl,
        reviews =
            currentTourData?.recentReviews.orEmpty().map { review ->
                TourDetailReviewUiModel(
                    id = review.id,
                    reviewerName = review.reviewerName,
                    comment = review.comment,
                    rating = review.rating,
                    reviewerImageResId = review.reviewerImageResId,
                )
            },
    )
}
