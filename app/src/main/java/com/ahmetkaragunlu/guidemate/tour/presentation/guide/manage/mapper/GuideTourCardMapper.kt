package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.mapper

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.location.locale.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.tour.presentation.formatting.formatTourDateTime
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCard
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourCardUiModel
import java.time.Instant
import java.util.Locale

fun GuideTourCard.toGuideTourCardUiModel(
    now: Instant = Instant.now(),
): GuideTourCardUiModel {
    val locale = Locale.getDefault()
    val country = LocaleSelectionCatalog.country(countryCode, locale)?.displayName ?: countryCode
    val languages = languageCodes.mapNotNull { LocaleSelectionCatalog.language(it, locale) }
    val effectiveStatus =
        if (
            sessionStatus != TourSessionStatus.CANCELLED &&
                sessionStatus != TourSessionStatus.COMPLETED &&
                !startsAt.plusSeconds(durationMinutes * 60L).isAfter(now)
        ) {
            TourSessionStatus.COMPLETED
        } else {
            sessionStatus
        }
    return GuideTourCardUiModel(
        id = sessionId,
        tourId = tourId,
        title = title,
        date = startsAt.formatTourDateTime(timeZoneId),
        location = listOf(cityName, country).filter(String::isNotBlank).joinToString(", "),
        imageResId = R.drawable.ic_image_unavailable,
        imageUrl = cover?.imageUrl,
        participantCount = bookedCount,
        capacity = capacity,
        languagesFlag = languages.joinToString(separator = "") { it.flagEmoji },
        languagesText = languages.joinToString(separator = ", ") { it.shortCode },
        category = category,
        priceMinor = priceMinor,
        rating = averageRating.takeIf { reviewCount > 0 },
        reviewCount = reviewCount.takeIf { it > 0 },
        approvalStatus = approvalStatus,
        sessionStatus = effectiveStatus,
        rejectionReason = rejectionReason,
        canArchive = canArchive,
        earningsMinor = netEarningsMinor,
    )
}
