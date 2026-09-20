package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.location.model.CityOption
import com.ahmetkaragunlu.guidemate.common.location.model.CountryOption
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.common.result.DataResult
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

        private val tourRequest = SearchRequestTracker<TourSearchQuery>()
        private val guideRequest = SearchRequestTracker<String>()

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
            tourRequest.job?.cancel()
            val query = _uiState.value.toTourSearchQuery()
            val generation = ++tourRequest.generation
            tourRequest.job =
                loadTours(query = query, page = 0, append = false, generation = generation)
        }

        fun loadMoreTours() {
            val state = _uiState.value
            if (!state.tours.canLoadMore ||
                state.tours.isLoadingMore ||
                tourRequest.job?.isActive == true ||
                tourRequest.loadedQuery != state.toTourSearchQuery()
            ) {
                return
            }
            val query = state.toTourSearchQuery()
            tourRequest.job =
                loadTours(
                    query = query,
                    page = tourRequest.currentPage + 1,
                    append = true,
                    generation = tourRequest.generation,
                )
        }

        fun refreshGuides() {
            guideRequest.job?.cancel()
            val query = _uiState.value.guides.searchQuery.trim()
            val generation = ++guideRequest.generation
            guideRequest.job =
                loadGuides(query = query, page = 0, append = false, generation = generation)
        }

        fun loadMoreGuides() {
            val state = _uiState.value
            if (!state.guides.canLoadMore ||
                state.guides.isLoadingMore ||
                guideRequest.job?.isActive == true ||
                guideRequest.loadedQuery != state.guides.searchQuery.trim()
            ) {
                return
            }
            val query = state.guides.searchQuery.trim()
            guideRequest.job =
                loadGuides(
                    query = query,
                    page = guideRequest.currentPage + 1,
                    append = true,
                    generation = guideRequest.generation,
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
                ) { tab, query, filters ->
                    TourSearchTrigger(
                        selectedTab = tab,
                        query = query,
                        filters = filters,
                    )
                }.distinctUntilChanged()
                    .collectLatest { trigger ->
                        delay(SEARCH_DEBOUNCE_MILLIS)
                        if (trigger.selectedTab == ExploreTab.TOURS) refreshTours()
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
                _uiState.update { it.startTourLoad(append) }
                if (!append) {
                    tourRequest.currentPage = 0
                    tourRequest.loadedQuery = null
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
                        tourRequest.currentPage = result.data.page
                        tourRequest.loadedQuery = query
                        _uiState.update { current ->
                            current.completeTourLoad(
                                results = mapped,
                                resultCount = result.data.totalElements,
                                canLoadMore = !result.data.isLast,
                                append = append,
                            )
                        }
                    }
                    is DataResult.Error -> {
                        if (!isCurrentTourRequest(generation, query)) return@launch
                        _uiState.update { it.failTourLoad(append) }
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
                _uiState.update { it.startGuideLoad(append) }
                if (!append) {
                    guideRequest.currentPage = 0
                    guideRequest.loadedQuery = null
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
                        guideRequest.currentPage = result.data.page
                        guideRequest.loadedQuery = query
                        _uiState.update { current ->
                            current.completeGuideLoad(
                                results = mapped,
                                canLoadMore = !result.data.isLast,
                                append = append,
                            )
                        }
                    }
                    is DataResult.Error -> {
                        if (!isCurrentGuideRequest(generation, query)) return@launch
                        _uiState.update { it.failGuideLoad(append) }
                    }
                }
            }

        private fun isCurrentTourRequest(
            generation: Long,
            query: TourSearchQuery,
        ): Boolean =
            generation == tourRequest.generation && query == _uiState.value.toTourSearchQuery()

        private fun isCurrentGuideRequest(
            generation: Long,
            query: String,
        ): Boolean =
            generation == guideRequest.generation &&
                query == _uiState.value.guides.searchQuery.trim()

        private fun updateDraftFilters(transform: TourFilterUiState.() -> TourFilterUiState) {
            _uiState.update { it.copy(draftFilters = it.draftFilters.transform()) }
        }

        private companion object {
            const val TOUR_PAGE_SIZE = 20
            const val GUIDE_PAGE_SIZE = 20
            const val SEARCH_DEBOUNCE_MILLIS = 350L
        }
    }

private data class TourSearchTrigger(
    val selectedTab: ExploreTab,
    val query: String,
    val filters: TourFilterUiState,
)

private class SearchRequestTracker<Query> {
    var job: Job? = null
    var currentPage: Int = 0
    var generation: Long = 0L
    var loadedQuery: Query? = null
}
