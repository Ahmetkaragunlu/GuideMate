package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.location.model.CityOption
import com.ahmetkaragunlu.guidemate.common.location.model.CountryOption
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.ExploreTab
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.ExploreUiState
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.TourFilterUiState
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.profile.presentation.mapper.toGuideResultUiModel
import com.ahmetkaragunlu.guidemate.review.domain.repository.ReviewRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchQuery
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.category.TourCategoryCatalog
import com.ahmetkaragunlu.guidemate.tour.presentation.mapper.toSearchResultUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToLong
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TouristExploreViewModel
    @Inject
    constructor(
        private val tourRepository: TourDiscoveryRepository,
        private val profileRepository: GuideProfileRepository,
        private val reviewRepository: ReviewRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ExploreUiState())
        val uiState = _uiState.asStateFlow()

        private var tourRequestJob: Job? = null
        private var guideRequestJob: Job? = null
        private var currentTourPage = 0
        private var currentGuidePage = 0

        val categories = TourCategoryCatalog.filterOptions

        init {
            observeTourSearch()
            observeGuideSearch()
            observeReviewChanges()
        }

        private fun observeReviewChanges() {
            viewModelScope.launch {
                reviewRepository.reviewChanges.collect {
                    refreshTours()
                    refreshGuides()
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

        fun refreshTours() {
            tourRequestJob?.cancel()
            tourRequestJob = loadTours(page = 0, append = false)
        }

        fun loadMoreTours() {
            val state = _uiState.value
            if (!state.canLoadMoreTours || state.isLoadingMoreTours || tourRequestJob?.isActive == true) {
                return
            }
            tourRequestJob = loadTours(page = currentTourPage + 1, append = true)
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
            updateDraftFilters { copy(selectedCategory = category) }
        }

        fun updateSelectedRating(rating: Int) {
            updateDraftFilters { copy(selectedRating = rating) }
        }

        fun updatePriceRange(range: ClosedFloatingPointRange<Float>) {
            updateDraftFilters { copy(priceRange = range) }
        }

        fun updateSelectedCountry(country: CountryOption) {
            updateDraftFilters {
                copy(
                    selectedCountry = country,
                    selectedCity = selectedCity?.takeIf { it.countryCode == country.code },
                )
            }
        }

        fun updateSelectedCity(city: CityOption) {
            updateDraftFilters { copy(selectedCity = city) }
        }

        fun updateSelectedLanguages(languages: List<LanguageOption>) {
            updateDraftFilters { copy(selectedLanguages = languages) }
        }

        fun clearFilters() {
            _uiState.update { it.copy(draftFilters = TourFilterUiState()) }
        }

        fun applyFilters() {
            _uiState.update { it.copy(appliedFilters = it.draftFilters) }
        }

        fun clearSearchAndFilters() {
            _uiState.update {
                it.copy(
                    toursSearchQuery = "",
                    draftFilters = TourFilterUiState(),
                    appliedFilters = TourFilterUiState(),
                )
            }
        }

        private fun observeTourSearch() {
            viewModelScope.launch {
                combine(
                    _uiState.map { it.selectedTab },
                    _uiState.map { it.toursSearchQuery.trim() },
                    _uiState.map { it.appliedFilters },
                    ::Triple,
                ).distinctUntilChanged()
                    .collectLatest { (tab, _, _) ->
                        delay(SEARCH_DEBOUNCE_MILLIS)
                        if (tab == ExploreTab.TOURS) refreshTours()
                    }
            }
        }

        private fun observeGuideSearch() {
            viewModelScope.launch {
                combine(
                    _uiState.map { it.selectedTab },
                    _uiState.map { it.guidesSearchQuery.trim() },
                    ::Pair,
                ).distinctUntilChanged()
                    .collectLatest { (tab, _) ->
                        delay(SEARCH_DEBOUNCE_MILLIS)
                        if (tab == ExploreTab.GUIDES) refreshGuides()
                    }
            }
        }

        private fun loadTours(
            page: Int,
            append: Boolean,
        ): Job =
            viewModelScope.launch {
                if (append) {
                    _uiState.update {
                        it.copy(isLoadingMoreTours = true, tourAppendFailed = false)
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            tourResultsLoadState = ContentLoadState.LOADING,
                            isLoadingMoreTours = false,
                            tourAppendFailed = false,
                        )
                    }
                }

                val state = _uiState.value
                when (
                    val result =
                        tourRepository.searchTours(
                            query = state.toTourSearchQuery(),
                            page = page,
                            size = TOUR_PAGE_SIZE,
                        )
                ) {
                    is DataResult.Success -> {
                        val mapped = result.data.items.map { it.toSearchResultUiModel() }
                        currentTourPage = result.data.page
                        _uiState.update { current ->
                            current.copy(
                                tourResults = if (append) current.tourResults + mapped else mapped,
                                tourResultCount = result.data.totalElements,
                                tourResultsLoadState = ContentLoadState.CONTENT,
                                isLoadingMoreTours = false,
                                tourAppendFailed = false,
                                canLoadMoreTours = !result.data.isLast,
                            )
                        }
                    }
                    is DataResult.Error -> {
                        _uiState.update { current ->
                            current.copy(
                                tourResultsLoadState =
                                    if (append || current.tourResults.isNotEmpty()) {
                                        ContentLoadState.CONTENT
                                    } else {
                                        ContentLoadState.ERROR
                                    },
                                isLoadingMoreTours = false,
                                tourAppendFailed = append,
                            )
                        }
                    }
                }
            }

        private fun loadGuides(
            page: Int,
            append: Boolean,
        ): Job =
            viewModelScope.launch {
                if (append) {
                    _uiState.update {
                        it.copy(isLoadingMoreGuides = true, guideAppendFailed = false)
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
                                guideResults = if (append) current.guideResults + mapped else mapped,
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

        private fun updateDraftFilters(transform: TourFilterUiState.() -> TourFilterUiState) {
            _uiState.update { it.copy(draftFilters = it.draftFilters.transform()) }
        }

        private fun ExploreUiState.toTourSearchQuery(): TourSearchQuery {
            val filters = appliedFilters
            return TourSearchQuery(
                text = toursSearchQuery,
                countryCode = filters.selectedCountry?.code,
                cityPlaceId = filters.selectedCity?.placeId,
                categoryCode = filters.selectedCategory?.code,
                languageCodes = filters.selectedLanguages.map { it.code },
                minimumRating = filters.selectedRating.takeIf { it > 0 }?.toDouble(),
                minimumPriceMinor =
                    filters.priceRange.start
                        .takeIf { it > 0f }
                        ?.toMinorUnits(),
                maximumPriceMinor =
                    filters.priceRange.endInclusive
                        .takeIf { it < TourFilterUiState.MAX_PRICE }
                        ?.toMinorUnits(),
            )
        }

        private companion object {
            const val TOUR_PAGE_SIZE = 20
            const val GUIDE_PAGE_SIZE = 20
            const val SEARCH_DEBOUNCE_MILLIS = 350L
        }
    }

private fun Float.toMinorUnits(): Long = (toDouble() * 100).roundToLong()
