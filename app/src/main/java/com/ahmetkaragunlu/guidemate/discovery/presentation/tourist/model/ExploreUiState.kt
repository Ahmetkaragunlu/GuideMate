package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model

import com.ahmetkaragunlu.guidemate.common.location.model.CityOption
import com.ahmetkaragunlu.guidemate.common.location.model.CountryOption
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideResultUiModel
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.model.TourSearchResultUiModel

data class ExploreUiState(
    val selectedTab: ExploreTab = ExploreTab.TOURS,
    val draftFilters: TourFilterUiState = TourFilterUiState(),
    val appliedFilters: TourFilterUiState = TourFilterUiState(),
    val toursSearchQuery: String = "",
    val tourResults: List<TourSearchResultUiModel> = emptyList(),
    val tourResultCount: Long = 0,
    val tourResultsLoadState: ContentLoadState = ContentLoadState.LOADING,
    val isLoadingMoreTours: Boolean = false,
    val tourAppendFailed: Boolean = false,
    val canLoadMoreTours: Boolean = false,
    val guidesSearchQuery: String = "",
    val guideResults: List<GuideResultUiModel> = emptyList(),
    val guideResultsLoadState: ContentLoadState = ContentLoadState.LOADING,
    val isLoadingMoreGuides: Boolean = false,
    val guideAppendFailed: Boolean = false,
    val canLoadMoreGuides: Boolean = false,
)

data class TourFilterUiState(
    val selectedCategory: TourCategory? = null,
    val selectedRating: Int = 0,
    val priceRange: ClosedFloatingPointRange<Float> = DEFAULT_PRICE_RANGE,
    val selectedCountry: CountryOption? = null,
    val selectedCity: CityOption? = null,
    val selectedLanguages: List<LanguageOption> = emptyList(),
) {
    companion object {
        const val MAX_PRICE = 1_000f
        val DEFAULT_PRICE_RANGE = 0f..MAX_PRICE
    }
}
