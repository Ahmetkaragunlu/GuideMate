package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.location.model.CityOption
import com.ahmetkaragunlu.guidemate.common.location.model.CountryOption
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.category.TourCategoryCatalog
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.ExploreTab
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.ExploreUiState
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.profile.presentation.mapper.toGuideResultUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TouristExploreViewModel
    @Inject
    constructor(
        private val profileRepository: GuideProfileRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ExploreUiState())
        val uiState = _uiState.asStateFlow()
        private var guideRequestJob: Job? = null
        private var currentGuidePage = 0

        val categories = TourCategoryCatalog.filterOptions

        init {
            viewModelScope.launch {
                combine(
                    _uiState.map { it.selectedTab },
                    _uiState.map { it.guidesSearchQuery.trim() },
                    ::Pair,
                ).distinctUntilChanged()
                    .collectLatest { (tab, _) ->
                        delay(GUIDE_SEARCH_DEBOUNCE_MILLIS)
                        if (tab == ExploreTab.GUIDES) refreshGuides()
                    }
            }
        }

        fun updateSelectedTab(tab: ExploreTab) {
            _uiState.update { it.copy(selectedTab = tab) }
        }

        fun updateToursSearchQuery(query: String) {
            _uiState.update { it.copy(toursSearchQuery = query) }
        }

        fun updateGuidesSearchQuery(query: String) {
            _uiState.update { it.copy(guidesSearchQuery = query) }
        }

        fun refreshGuides() {
            guideRequestJob?.cancel()
            guideRequestJob = loadGuides(page = 0, append = false)
        }

        fun loadMoreGuides() {
            val state = _uiState.value
            if (!state.canLoadMoreGuides || state.isLoadingMoreGuides || guideRequestJob?.isActive == true) {
                return
            }
            guideRequestJob = loadGuides(page = currentGuidePage + 1, append = true)
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
                current.copy(
                    selectedCategory = null,
                    selectedRating = 0,
                    priceRange = 0f..500f,
                    selectedCountry = null,
                    selectedCity = null,
                    selectedLanguages = emptyList(),
                )
            }
        }

        private fun loadGuides(
            page: Int,
            append: Boolean,
        ): Job =
            viewModelScope.launch {
                if (append) {
                    _uiState.update {
                        it.copy(
                            isLoadingMoreGuides = true,
                            guideAppendFailed = false,
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            guideResultsLoadState = ContentLoadState.LOADING,
                            isLoadingMoreGuides = false,
                            guideAppendFailed = false,
                        )
                    }
                }

                when (
                    val result =
                        profileRepository.searchGuides(
                            query = _uiState.value.guidesSearchQuery,
                            page = page,
                            size = GUIDE_PAGE_SIZE,
                        )
                ) {
                    is DataResult.Success -> {
                        val mapped = result.data.items.map { it.toGuideResultUiModel() }
                        currentGuidePage = result.data.page
                        _uiState.update { current ->
                            current.copy(
                                guideResults =
                                    if (append) current.guideResults + mapped else mapped,
                                guideResultsLoadState = ContentLoadState.CONTENT,
                                isLoadingMoreGuides = false,
                                guideAppendFailed = false,
                                canLoadMoreGuides = !result.data.isLast,
                            )
                        }
                    }
                    is DataResult.Error -> {
                        _uiState.update { current ->
                            current.copy(
                                guideResultsLoadState =
                                    if (append || current.guideResults.isNotEmpty()) {
                                        ContentLoadState.CONTENT
                                    } else {
                                        ContentLoadState.ERROR
                                    },
                                isLoadingMoreGuides = false,
                                guideAppendFailed = append,
                            )
                        }
                    }
                }
            }

        private companion object {
            const val GUIDE_PAGE_SIZE = 20
            const val GUIDE_SEARCH_DEBOUNCE_MILLIS = 350L
        }
    }
