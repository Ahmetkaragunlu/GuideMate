package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.mapper

import com.ahmetkaragunlu.guidemate.tour.presentation.formatting.formatTourDateTime
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.effectiveStatus
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourCardUiModel
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
