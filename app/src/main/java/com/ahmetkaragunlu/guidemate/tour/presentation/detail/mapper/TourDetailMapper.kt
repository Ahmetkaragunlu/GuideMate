package com.ahmetkaragunlu.guidemate.tour.presentation.detail.mapper

import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailReviewUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailStatus
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.formatting.formatTourDateTime
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.effectiveStatus
import java.time.Instant

fun TourWithSession.toTourDetailUiState(
    now: Instant = Instant.now(),
): TourDetailUiState =
    TourDetailUiState(
        sessionId = session.id,
        tourId = tour.id,
        title = tour.title,
        imageResId = tour.coverImageResId,
        imageUrl = tour.coverImageUrl,
        rating = tour.averageRating,
        reviewCount = tour.reviewCount,
        date = session.startsAt.formatTourDateTime(tour.timeZoneId),
        durationMinutes = session.durationMinutes,
        location = listOf(tour.city, tour.country).filter(String::isNotBlank).joinToString(", "),
        languagesFlag = tour.languages.joinToString(separator = " ") { it.flagEmoji },
        languagesText = tour.languages.joinToString(separator = ", ") { it.shortCode },
        category = tour.category,
        priceMinor = session.priceMinor,
        bookedCount = session.bookedCount,
        capacity = session.capacity,
        description = tour.description,
        meetingPoint = session.meetingPoint,
        sessionStatus =
            when (session.effectiveStatus(now)) {
                TourSessionStatus.CANCELLED -> TourDetailStatus.CANCELLED
                TourSessionStatus.COMPLETED -> TourDetailStatus.COMPLETED
                else -> null
            },
        cancellationReason = session.cancellationReason,
        guideId = tour.guide.id,
        guideName = tour.guide.displayName,
        guideImageResId = tour.guide.profileImageResId,
        guideImageUrl = tour.guide.profileImageUrl,
        reviews =
            tour.recentReviews.map { review ->
                TourDetailReviewUiModel(
                    id = review.id,
                    reviewerName = review.reviewerName,
                    comment = review.comment,
                    rating = review.rating,
                    reviewerImageResId = review.reviewerImageResId,
                    reviewerImageUrl = review.reviewerImageUrl,
                )
            },
    )
