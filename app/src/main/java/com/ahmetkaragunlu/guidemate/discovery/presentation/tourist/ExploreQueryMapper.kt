package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist

import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.ExploreUiState
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.TourFilterUiState
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchQuery
import kotlin.math.roundToLong

internal fun ExploreUiState.toTourSearchQuery(): TourSearchQuery {
    val filters = appliedFilters
    return TourSearchQuery(
        text = tours.searchQuery,
        countryCode = filters.selectedCountry?.code,
        cityPlaceId = filters.selectedCity?.placeId,
        categoryCode = filters.selectedCategory?.code,
        languageCodes = filters.selectedLanguages.map { it.code },
        minimumRating = filters.selectedRating.takeIf { it > 0 }?.toDouble(),
        minimumPriceMinor = filters.priceRange.start.takeIf { it > 0f }?.toMinorUnits(),
        maximumPriceMinor =
            filters.priceRange.endInclusive
                .takeIf { it < TourFilterUiState.MAX_PRICE }
                ?.toMinorUnits(),
    )
}

private fun Float.toMinorUnits(): Long = (toDouble() * 100).roundToLong()
