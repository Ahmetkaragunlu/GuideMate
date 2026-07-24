package com.ahmetkaragunlu.guidemate.screens.tourist.explore.model

import com.ahmetkaragunlu.guidemate.screens.common.selection.model.CityOption
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.CountryOption
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.LanguageOption
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategory

data class ExploreUiState(
    val selectedTab: ExploreTab = ExploreTab.TOURS,
    val selectedCategory: TourCategory? = null,
    val selectedRating: Int = 0,
    val priceRange: ClosedFloatingPointRange<Float> = 0f..500f,
    val selectedCountry: CountryOption? = null,
    val selectedCity: CityOption? = null,
    val selectedLanguages: List<LanguageOption> = emptyList(),
    val toursSearchQuery: String = "",
    val guidesSearchQuery: String = "",
)
