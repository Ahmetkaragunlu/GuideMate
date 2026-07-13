package com.ahmetkaragunlu.guidemate.screens.guide.tours.mapper

import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailReviewUiModel
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailStatus
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourSessionStatus
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourWithSession
import java.time.ZoneId

fun TourWithSession.toTourDetailUiState(): TourDetailUiState =
    TourDetailUiState(
        sessionId = session.id,
        tourId = tour.id,
        title = tour.title,
        imageResId = tour.coverImageResId,
        imageUrl = tour.coverImageUrl,
        rating = tour.averageRating,
        reviewCount = tour.reviewCount,
        date = tourDateFormatter.format(session.startsAt.atZone(ZoneId.systemDefault())),
        durationMinutes = session.durationMinutes,
        location = listOf(tour.city, tour.country).filter(String::isNotBlank).joinToString(", "),
        languagesFlag = tour.languages.joinToString(separator = " ") { it.flagEmoji },
        languagesText = tour.languages.joinToString(separator = ", ") { it.shortCode },
        category = tour.category,
        price = session.price,
        bookedCount = session.bookedCount,
        capacity = session.capacity,
        description = tour.description,
        meetingPoint = session.meetingPoint,
        sessionStatus =
            when (session.status) {
                TourSessionStatus.COMPLETED -> TourDetailStatus.COMPLETED
                TourSessionStatus.CANCELLED -> TourDetailStatus.CANCELLED
                else -> null
            },
        cancellationReason = session.cancellationReason,
        guideName = "Ahmet Yılmaz",
        reviews =
            tour.recentReviews.map { review ->
                TourDetailReviewUiModel(
                    id = review.id,
                    reviewerName = review.reviewerName,
                    comment = review.comment,
                    rating = review.rating,
                    reviewerImageResId = review.reviewerImageResId,
                )
            },
    )
