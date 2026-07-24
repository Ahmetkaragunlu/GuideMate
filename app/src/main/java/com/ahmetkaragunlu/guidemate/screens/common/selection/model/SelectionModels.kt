package com.ahmetkaragunlu.guidemate.screens.common.selection.model

import java.util.Locale

data class CountryOption(
    val code: String,
    val displayName: String,
)

data class LanguageOption(
    val code: String,
    val displayName: String,
    val flagEmoji: String,
) {
    val chipLabel: String
        get() = "$flagEmoji $displayName"

    val shortCode: String
        get() = code.uppercase(Locale.ROOT)
}

data class CitySearchResult(
    val placeId: String,
    val primaryText: String,
    val secondaryText: String,
)

data class CityOption(
    val placeId: String,
    val displayName: String,
    val countryCode: String,
)

data class LocationOption(
    val country: CountryOption,
    val city: CityOption,
)
