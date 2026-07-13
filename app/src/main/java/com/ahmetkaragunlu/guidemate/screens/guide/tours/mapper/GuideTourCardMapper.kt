package com.ahmetkaragunlu.guidemate.screens.guide.tours.mapper

import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourWithSession
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

internal val tourDateFormatter =
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)

fun TourWithSession.toGuideTourCardUiModel(): GuideTourCardUiModel =
    GuideTourCardUiModel(
        id = session.id,
        tourId = tour.id,
        title = tour.title,
        date = tourDateFormatter.format(session.startsAt.atZone(ZoneId.systemDefault())),
        location = listOf(tour.city, tour.country).filter(String::isNotBlank).joinToString(", "),
        imageResId = tour.coverImageResId,
        imageUrl = tour.coverImageUrl,
        participantCount = session.bookedCount,
        capacity = session.capacity,
        languagesFlag = tour.languages.joinToString(separator = "") { it.flagEmoji },
        languagesText = tour.languages.joinToString(separator = ", ") { it.shortCode },
        category = tour.category,
        price = session.price,
        rating = tour.averageRating,
        reviewCount = tour.reviewCount.takeIf { it > 0 },
        approvalStatus = tour.approvalStatus,
        sessionStatus = session.status,
        rejectionReason = tour.rejectionReason,
        canArchive =
            tour.approvalStatus == TourApprovalStatus.REJECTED &&
                tour.publishedAt == null,
        earnings = session.earnings,
    )
