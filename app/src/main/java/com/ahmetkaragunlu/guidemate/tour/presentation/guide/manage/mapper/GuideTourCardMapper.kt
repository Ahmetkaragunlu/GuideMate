package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.mapper

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.location.locale.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCard
import com.ahmetkaragunlu.guidemate.tour.presentation.formatting.formatTourDateTime
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourCardLanguagesUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourCardMediaUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourCardPricingUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourCardRatingUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourCardStatusUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourCardUiModel
import java.util.Locale

fun GuideTourCard.toGuideTourCardUiModel(): GuideTourCardUiModel {
    val locale = Locale.getDefault()
    val country =
        LocaleSelectionCatalog.country(schedule.countryCode, locale)?.displayName
            ?: schedule.countryCode
    val languages = languageCodes.mapNotNull { LocaleSelectionCatalog.language(it, locale) }
    return GuideTourCardUiModel(
        id = sessionId,
        tourId = tourId,
        title = title,
        date = schedule.startsAt.formatTourDateTime(schedule.timeZoneId),
        location =
            listOf(schedule.cityName, country)
                .filter(String::isNotBlank)
                .joinToString(", "),
        media =
            GuideTourCardMediaUiModel(
                imageResId = R.drawable.ic_image_unavailable,
                imageUrl = cover?.imageUrl,
            ),
        participantCount = stats.bookedCount,
        languages =
            GuideTourCardLanguagesUiModel(
                flags = languages.joinToString(separator = "") { it.flagEmoji },
                shortCodes = languages.joinToString(separator = ", ") { it.shortCode },
            ),
        category = category,
        pricing =
            GuideTourCardPricingUiModel(
                priceMinor = pricing.priceMinor,
                earningsMinor = pricing.netEarningsMinor,
            ),
        rating =
            stats.averageRating
                .takeIf { stats.reviewCount > 0 }
                ?.let { averageRating ->
                    GuideTourCardRatingUiModel(
                        value = averageRating,
                        reviewCount = stats.reviewCount,
                    )
                },
        status =
            GuideTourCardStatusUiModel(
                approvalStatus = approvalStatus,
                sessionStatus = sessionStatus,
                rejectionReason = rejectionReason,
                canArchive = canArchive,
            ),
    )
}
