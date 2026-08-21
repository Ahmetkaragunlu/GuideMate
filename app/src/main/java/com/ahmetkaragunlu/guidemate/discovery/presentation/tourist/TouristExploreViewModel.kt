package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.common.location.model.CityOption
import com.ahmetkaragunlu.guidemate.common.location.model.CountryOption
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.category.TourCategoryCatalog
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.ExploreTab
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.ExploreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TouristExploreViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(ExploreUiState())
        val uiState = _uiState.asStateFlow()

        val categories = TourCategoryCatalog.filterOptions

        fun updateSelectedTab(tab: ExploreTab) {
            _uiState.update { it.copy(selectedTab = tab) }
        }

        fun updateToursSearchQuery(query: String) {
            _uiState.update { it.copy(toursSearchQuery = query) }
        }

        fun updateGuidesSearchQuery(query: String) {
            _uiState.update { it.copy(guidesSearchQuery = query) }
        }

        fun updateSelectedCategory(category: TourCategory?) {
            _uiState.update { it.copy(selectedCategory = category) }
        }

        fun updateSelectedRating(rating: Int) {
            _uiState.update { it.copy(selectedRating = rating) }
        }

        fun updatePriceRange(range: ClosedFloatingPointRange<Float>) {
            _uiState.update { it.copy(priceRange = range) }
        }

        fun updateSelectedCountry(country: CountryOption) {
            _uiState.update { current ->
                current.copy(
                    selectedCountry = country,
                    selectedCity = current.selectedCity?.takeIf { it.countryCode == country.code },
                )
            }
        }

        fun updateSelectedCity(city: CityOption) {
            _uiState.update { it.copy(selectedCity = city) }
        }

        fun updateSelectedLanguages(languages: List<LanguageOption>) {
            _uiState.update { it.copy(selectedLanguages = languages) }
        }

        fun clearFilters() {
            _uiState.update { current ->
                ExploreUiState(
                    selectedTab = current.selectedTab,
                    toursSearchQuery = current.toursSearchQuery,
                    guidesSearchQuery = current.guidesSearchQuery,
                )
            }
        }
    }
