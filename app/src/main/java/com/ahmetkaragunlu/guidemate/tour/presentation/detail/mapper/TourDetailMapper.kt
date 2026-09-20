package com.ahmetkaragunlu.guidemate.tour.presentation.detail.mapper

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailGuideUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailReviewUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailSessionUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailStatus
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailTourUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.formatting.formatTourDateTime
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus

fun TourWithSession.toTourDetailUiState(): TourDetailUiState =
    TourDetailUiState(
        tour =
            TourDetailTourUiState(
                id = tour.id,
                title = tour.title,
                imageResId = R.drawable.ic_image_unavailable,
                imageUrl = tour.cover?.imageUrl,
                rating = tour.reviews.averageRating,
                reviewCount = tour.reviews.reviewCount,
                location =
                    listOf(tour.location.city, tour.location.country)
                        .filter(String::isNotBlank)
                        .joinToString(", "),
                languagesFlag = tour.languages.joinToString(separator = " ") { it.flagEmoji },
                languagesText = tour.languages.joinToString(separator = ", ") { it.shortCode },
                category = tour.category,
                description = tour.description,
            ),
        session =
            TourDetailSessionUiState(
                sessionId = session.id,
                date = session.startsAt.formatTourDateTime(tour.location.timeZoneId),
                durationMinutes = session.durationMinutes,
                priceMinor = session.priceMinor,
                bookedCount = session.bookedCount,
                capacity = session.capacity,
                meetingPoint = session.meetingPoint,
                status =
                    when (session.status) {
                        TourSessionStatus.CANCELLED -> TourDetailStatus.CANCELLED
                        TourSessionStatus.COMPLETED -> TourDetailStatus.COMPLETED
                        TourSessionStatus.EXPIRED -> TourDetailStatus.EXPIRED
                        else -> null
                    },
                cancellationReason = session.cancellationReason,
            ),
        guide =
            TourDetailGuideUiState(
                id = tour.guide.id,
                name = tour.guide.displayName,
                imageResId = R.drawable.ic_default_avatar,
                imageUrl = tour.guide.profileImageUrl,
            ),
        reviews =
            tour.reviews.recentReviews.map { review ->
                TourDetailReviewUiModel(
                    id = review.id,
                    reviewerName = review.reviewerName,
                    comment = review.comment,
                    rating = review.rating,
                    reviewerImageResId = R.drawable.ic_default_avatar,
                    reviewerImageUrl = review.reviewerImageUrl,
                )
            },
    )
