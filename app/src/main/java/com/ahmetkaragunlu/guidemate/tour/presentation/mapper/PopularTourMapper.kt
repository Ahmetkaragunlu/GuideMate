package com.ahmetkaragunlu.guidemate.tour.presentation.mapper

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.location.data.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchItem
import com.ahmetkaragunlu.guidemate.tour.presentation.formatting.formatTourDateTime
import com.ahmetkaragunlu.guidemate.tour.presentation.model.PopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.model.TourSearchResultUiModel
import java.util.Locale

fun TourSearchItem.toPopularTourCardUiModel(
    locale: Locale = Locale.getDefault(),
): PopularTourCardUiModel {
    val languages = localizedLanguages(locale)
    return PopularTourCardUiModel(
        id = sessionId,
        title = title,
        imageResId = R.drawable.example,
        imageUrl = cover.imageUrl,
        rating = averageRating?.toString() ?: "-",
        reviewCount = "($reviewCount)",
        priceMinor = priceMinor,
        languagesFlag = languages.joinToString(separator = " ") { it.flagEmoji },
        languagesText = languages.joinToString(separator = ", ") { it.shortCode },
        guideName = guide.displayName,
        guideImageResId = guide.profileImageResId,
        guideImageUrl = guide.profileImageUrl,
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
        imageResId = R.drawable.example,
        imageUrl = cover.imageUrl,
        rating = averageRating,
        reviewCount = reviewCount,
        priceMinor = priceMinor,
        date = startsAt.formatTourDateTime(timeZoneId),
        location = listOf(cityName, country).filter(String::isNotBlank).joinToString(", "),
        languagesFlag = languages.joinToString(separator = " ") { it.flagEmoji },
        languagesText = languages.joinToString(separator = ", ") { it.shortCode },
        availableCapacity = availableCapacity,
        guideName = guide.displayName,
        guideImageResId = guide.profileImageResId,
        guideImageUrl = guide.profileImageUrl,
    )
}

private fun TourSearchItem.localizedLanguages(locale: Locale) =
    languageCodes.map { code ->
        LocaleSelectionCatalog.language(code, locale)
            ?: com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption(
                code = code,
                displayName = code.uppercase(Locale.ROOT),
                flagEmoji = "🌐",
            )
    }
