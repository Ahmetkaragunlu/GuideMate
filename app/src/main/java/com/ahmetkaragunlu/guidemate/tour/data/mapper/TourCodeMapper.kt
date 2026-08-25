package com.ahmetkaragunlu.guidemate.tour.data.mapper

import com.ahmetkaragunlu.guidemate.common.location.locale.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import java.util.Locale

fun String.toTourCategory(): TourCategory =
    TourCategory.entries.first { category -> category.code.equals(this, ignoreCase = true) }

fun String.toTourLanguage(locale: Locale): TourLanguage {
    val language = LocaleSelectionCatalog.language(this, locale)
    return TourLanguage(
        code = this,
        flagEmoji = language?.flagEmoji ?: "🌐",
        displayName = language?.displayName ?: uppercase(Locale.ROOT),
        shortCode = language?.shortCode ?: uppercase(Locale.ROOT),
    )
}
