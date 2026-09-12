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
        private var tourRequestGeneration = 0L
        private var guideRequestGeneration = 0L
        private var loadedTourQuery: TourSearchQuery? = null
        private var loadedGuideQuery: String? = null

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
            _uiState.update { it.copy(tours = it.tours.copy(searchQuery = query)) }
        }

        fun updateGuidesSearchQuery(query: String) {
            _uiState.update { it.copy(guides = it.guides.copy(searchQuery = query)) }
        }

        fun refreshTours() {
            tourRequestJob?.cancel()
            val query = _uiState.value.toTourSearchQuery()
            val generation = ++tourRequestGeneration
            tourRequestJob = loadTours(query = query, page = 0, append = false, generation = generation)
        }

        fun loadMoreTours() {
            val state = _uiState.value
            if (!state.tours.canLoadMore ||
                state.tours.isLoadingMore ||
                tourRequestJob?.isActive == true ||
                loadedTourQuery != state.toTourSearchQuery()
            ) {
                return
            }
            val query = state.toTourSearchQuery()
            tourRequestJob =
                loadTours(
                    query = query,
                    page = currentTourPage + 1,
                    append = true,
                    generation = tourRequestGeneration,
                )
        }

        fun refreshGuides() {
            guideRequestJob?.cancel()
            val query = _uiState.value.guides.searchQuery.trim()
            val generation = ++guideRequestGeneration
            guideRequestJob =
                loadGuides(query = query, page = 0, append = false, generation = generation)
        }

        fun loadMoreGuides() {
            val state = _uiState.value
            if (!state.guides.canLoadMore ||
                state.guides.isLoadingMore ||
                guideRequestJob?.isActive == true ||
                loadedGuideQuery != state.guides.searchQuery.trim()
            ) {
                return
            }
            val query = state.guides.searchQuery.trim()
            guideRequestJob =
                loadGuides(
                    query = query,
                    page = currentGuidePage + 1,
                    append = true,
                    generation = guideRequestGeneration,
                )
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

        fun beginFilterEditing() {
            _uiState.update { it.copy(draftFilters = it.appliedFilters) }
        }

        fun cancelFilterEditing() {
            _uiState.update { it.copy(draftFilters = it.appliedFilters) }
        }

        fun applyFilters() {
            _uiState.update { it.copy(appliedFilters = it.draftFilters) }
        }

        fun clearSearchAndFilters() {
            _uiState.update {
                it.copy(
                    tours = it.tours.copy(searchQuery = ""),
                    draftFilters = TourFilterUiState(),
                    appliedFilters = TourFilterUiState(),
                )
            }
        }

        private fun observeTourSearch() {
            viewModelScope.launch {
                combine(
                    _uiState.map { it.selectedTab },
                    _uiState.map { it.tours.searchQuery.trim() },
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
                    _uiState.map { it.guides.searchQuery.trim() },
                    ::Pair,
                ).distinctUntilChanged()
                    .collectLatest { (tab, _) ->
                        delay(SEARCH_DEBOUNCE_MILLIS)
                        if (tab == ExploreTab.GUIDES) refreshGuides()
                    }
            }
        }

        private fun loadTours(
            query: TourSearchQuery,
            page: Int,
            append: Boolean,
            generation: Long,
        ): Job =
            viewModelScope.launch {
                if (append) {
                    _uiState.update {
                        it.copy(
                            tours = it.tours.copy(isLoadingMore = true, appendFailed = false),
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            tours =
                                it.tours.copy(
                                    loadState = ContentLoadState.LOADING,
                                    results = emptyList(),
                                    resultCount = 0,
                                    isLoadingMore = false,
                                    appendFailed = false,
                                    canLoadMore = false,
                                ),
                        )
                    }
                    currentTourPage = 0
                    loadedTourQuery = null
                }

                when (
                    val result =
                        tourRepository.searchTours(
                            query = query,
                            page = page,
                            size = TOUR_PAGE_SIZE,
                        )
                ) {
                    is DataResult.Success -> {
                        if (!isCurrentTourRequest(generation, query)) return@launch
                        val mapped = result.data.items.map { it.toSearchResultUiModel() }
                        currentTourPage = result.data.page
                        loadedTourQuery = query
                        _uiState.update { current ->
                            current.copy(
                                tours =
                                    current.tours.copy(
                                        results =
                                            if (append) current.tours.results + mapped else mapped,
                                        resultCount = result.data.totalElements,
                                        loadState = ContentLoadState.CONTENT,
                                        isLoadingMore = false,
                                        appendFailed = false,
                                        canLoadMore = !result.data.isLast,
                                    ),
                            )
                        }
                    }
                    is DataResult.Error -> {
                        if (!isCurrentTourRequest(generation, query)) return@launch
                        _uiState.update { current ->
                            current.copy(
                                tours =
                                    current.tours.copy(
                                        loadState =
                                            if (append || current.tours.results.isNotEmpty()) {
                                                ContentLoadState.CONTENT
                                            } else {
                                                ContentLoadState.ERROR
                                            },
                                        isLoadingMore = false,
                                        appendFailed = append,
                                    ),
                            )
                        }
                    }
                }
            }

        private fun loadGuides(
            query: String,
            page: Int,
            append: Boolean,
            generation: Long,
        ): Job =
            viewModelScope.launch {
                if (append) {
                    _uiState.update {
                        it.copy(
                            guides = it.guides.copy(isLoadingMore = true, appendFailed = false),
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            guides =
                                it.guides.copy(
                                    loadState = ContentLoadState.LOADING,
                                    results = emptyList(),
                                    isLoadingMore = false,
                                    appendFailed = false,
                                    canLoadMore = false,
                                ),
                        )
                    }
                    currentGuidePage = 0
                    loadedGuideQuery = null
                }

                when (
                    val result =
                        profileRepository.searchGuides(
                            query = query,
                            page = page,
                            size = GUIDE_PAGE_SIZE,
                        )
                ) {
                    is DataResult.Success -> {
                        if (!isCurrentGuideRequest(generation, query)) return@launch
                        val mapped = result.data.items.map { it.toGuideResultUiModel() }
                        currentGuidePage = result.data.page
                        loadedGuideQuery = query
                        _uiState.update { current ->
                            current.copy(
                                guides =
                                    current.guides.copy(
                                        results =
                                            if (append) current.guides.results + mapped else mapped,
                                        loadState = ContentLoadState.CONTENT,
                                        isLoadingMore = false,
                                        appendFailed = false,
                                        canLoadMore = !result.data.isLast,
                                    ),
                            )
                        }
                    }
                    is DataResult.Error -> {
                        if (!isCurrentGuideRequest(generation, query)) return@launch
                        _uiState.update { current ->
                            current.copy(
                                guides =
                                    current.guides.copy(
                                        loadState =
                                            if (append || current.guides.results.isNotEmpty()) {
                                                ContentLoadState.CONTENT
                                            } else {
                                                ContentLoadState.ERROR
                                            },
                                        isLoadingMore = false,
                                        appendFailed = append,
                                    ),
                            )
                        }
                    }
                }
            }

        private fun isCurrentTourRequest(
            generation: Long,
            query: TourSearchQuery,
        ): Boolean =
            generation == tourRequestGeneration && query == _uiState.value.toTourSearchQuery()

        private fun isCurrentGuideRequest(
            generation: Long,
            query: String,
        ): Boolean =
            generation == guideRequestGeneration && query == _uiState.value.guides.searchQuery.trim()

        private fun updateDraftFilters(transform: TourFilterUiState.() -> TourFilterUiState) {
            _uiState.update { it.copy(draftFilters = it.draftFilters.transform()) }
        }

        private companion object {
            const val TOUR_PAGE_SIZE = 20
            const val GUIDE_PAGE_SIZE = 20
            const val SEARCH_DEBOUNCE_MILLIS = 350L
        }
    }
