package com.ahmetkaragunlu.guidemate.screens.guide.tours.mapper

import com.ahmetkaragunlu.guidemate.screens.common.tours.formatting.formatTourDateTime
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.session.effectiveStatus
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourCardUiModel
import java.time.Instant

fun TourWithSession.toGuideTourCardUiModel(
    now: Instant = Instant.now(),
): GuideTourCardUiModel =
    GuideTourCardUiModel(
        id = session.id,
        tourId = tour.id,
        title = tour.title,
        date = session.startsAt.formatTourDateTime(tour.timeZoneId),
        location = listOf(tour.city, tour.country).filter(String::isNotBlank).joinToString(", "),
        imageResId = tour.coverImageResId,
        imageUrl = tour.coverImageUrl,
        participantCount = session.bookedCount,
        capacity = session.capacity,
        languagesFlag = tour.languages.joinToString(separator = "") { it.flagEmoji },
        languagesText = tour.languages.joinToString(separator = ", ") { it.shortCode },
        category = tour.category,
        priceMinor = session.priceMinor,
        rating = tour.averageRating,
        reviewCount = tour.reviewCount.takeIf { it > 0 },
        approvalStatus = tour.approvalStatus,
        sessionStatus = session.effectiveStatus(now),
        rejectionReason = tour.rejectionReason,
        canArchive =
            tour.approvalStatus == TourApprovalStatus.REJECTED &&
                tour.publishedAt == null,
        earningsMinor = session.earningsMinor,
    )
