package com.ahmetkaragunlu.guidemate.tour.presentation.mapper

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.location.locale.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toRatingText
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchItem
import com.ahmetkaragunlu.guidemate.tour.presentation.formatting.formatTourDateTime
import com.ahmetkaragunlu.guidemate.tour.presentation.model.PopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.model.PopularTourRatingUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.model.TourCardGuideUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.model.TourCardLanguagesUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.model.TourCardMediaUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.model.TourSearchResultUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.model.TourSearchRatingUiModel
import java.util.Locale

fun TourSearchItem.toPopularTourCardUiModel(
    locale: Locale = Locale.getDefault(),
): PopularTourCardUiModel {
    val languages = localizedLanguages(locale)
    return PopularTourCardUiModel(
        id = sessionId,
        title = title,
        media =
            TourCardMediaUiModel(
                imageResId = R.drawable.ic_image_unavailable,
                imageUrl = cover.imageUrl,
            ),
        rating =
            PopularTourRatingUiModel(
                value = averageRating?.toRatingText(locale) ?: "-",
                reviewCount = "($reviewCount)",
            ),
        priceMinor = priceMinor,
        languages = languages.toTourCardLanguagesUiModel(),
        guide =
            TourCardGuideUiModel(
                name = guide.displayName,
                imageResId = R.drawable.ic_default_avatar,
                imageUrl = guide.profileImageUrl,
            ),
    )
}

fun TourSearchItem.toSearchResultUiModel(
    locale: Locale = Locale.getDefault(),
): TourSearchResultUiModel {
    val languages = localizedLanguages(locale)
    val country =
        LocaleSelectionCatalog.country(countryCode, locale)?.displayName
            ?: countryCode
    return TourSearchResultUiModel(
        sessionId = sessionId,
        title = title,
        media =
            TourCardMediaUiModel(
                imageResId = R.drawable.ic_image_unavailable,
                imageUrl = cover.imageUrl,
            ),
        rating =
            averageRating?.let { value ->
                TourSearchRatingUiModel(
                    value = value,
                    reviewCount = reviewCount,
                )
            },
        priceMinor = priceMinor,
        date = startsAt.formatTourDateTime(timeZoneId),
        location = listOf(cityName, country).filter(String::isNotBlank).joinToString(", "),
        languages = languages.toTourCardLanguagesUiModel(),
        availableCapacity = availableCapacity,
        guide =
            TourCardGuideUiModel(
                name = guide.displayName,
                imageResId = R.drawable.ic_default_avatar,
                imageUrl = guide.profileImageUrl,
            ),
    )
}

private fun List<LanguageOption>.toTourCardLanguagesUiModel() =
    TourCardLanguagesUiModel(
        flags = joinToString(separator = " ") { it.flagEmoji },
        shortCodes = joinToString(separator = ", ") { it.shortCode },
    )

private fun TourSearchItem.localizedLanguages(locale: Locale) =
    languageCodes.map { code ->
        LocaleSelectionCatalog.language(code, locale)
            ?: LanguageOption(
                code = code,
                displayName = code.uppercase(Locale.ROOT),
                flagEmoji = "🌐",
            )
    }
