package com.ahmetkaragunlu.guidemate.home.presentation.tourist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.home.presentation.tourist.model.TouristHomeUiState
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.profile.presentation.mapper.toGuideResultUiModel
import com.ahmetkaragunlu.guidemate.review.domain.repository.ReviewRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchQuery
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchSort
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.category.TourCategoryCatalog
import com.ahmetkaragunlu.guidemate.tour.presentation.mapper.toPopularTourCardUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TouristHomeViewModel
    @Inject
    constructor(
        userRepository: UserRepository,
        private val tourRepository: TourDiscoveryRepository,
        private val profileRepository: GuideProfileRepository,
        private val reviewRepository: ReviewRepository,
    ) : ViewModel() {
        val userName: StateFlow<String?> =
            userRepository.userState
                .map { it.firstName }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = null,
                )

        val categories = TourCategoryCatalog.filterOptions

        private val _uiState = MutableStateFlow(TouristHomeUiState())
        val uiState = _uiState.asStateFlow()

        private var popularToursJob: Job? = null
        private var bestGuidesJob: Job? = null

        init {
            refreshPopularTours()
            refreshBestGuides()
            observeReviewChanges()
        }

        private fun observeReviewChanges() {
            viewModelScope.launch {
                reviewRepository.reviewChanges.collect {
                    refreshPopularTours()
                    refreshBestGuides()
                }
            }
        }

        fun updateSelectedCategory(category: TourCategory?) {
            if (_uiState.value.selectedCategory == category) return
            _uiState.update { it.copy(selectedCategory = category) }
            refreshPopularTours()
        }

        fun refreshPopularTours() {
            popularToursJob?.cancel()
            popularToursJob =
                viewModelScope.launch {
                    _uiState.update { it.copy(popularToursLoadState = ContentLoadState.LOADING) }
                    val selectedCategory = _uiState.value.selectedCategory
                    val result =
                        if (selectedCategory == null) {
                            tourRepository.getPopularTours(page = 0, size = POPULAR_TOUR_LIMIT)
                        } else {
                            tourRepository.searchTours(
                                query =
                                    TourSearchQuery(
                                        categoryCode = selectedCategory.code,
                                        sort = TourSearchSort.RATING_DESC,
                                    ),
                                page = 0,
                                size = POPULAR_TOUR_LIMIT,
                            )
                        }
                    _uiState.update { current ->
                        when (result) {
                            is DataResult.Success ->
                                current.copy(
                                    popularTours =
                                        result.data.items.map { it.toPopularTourCardUiModel() },
                                    popularToursLoadState = ContentLoadState.CONTENT,
                                )
                            is DataResult.Error ->
                                current.copy(
                                    popularTours = emptyList(),
                                    popularToursLoadState = ContentLoadState.ERROR,
                                )
                        }
                    }
                }
        }

        fun refreshBestGuides() {
            if (bestGuidesJob?.isActive == true) return
            bestGuidesJob =
                viewModelScope.launch {
                    val hasContent = _uiState.value.bestGuides.isNotEmpty()
                    if (!hasContent) {
                        _uiState.update { it.copy(bestGuidesLoadState = ContentLoadState.LOADING) }
                    }
                    val result = profileRepository.getTopGuides(limit = 4)
                    _uiState.update { current ->
                        when (result) {
                            is DataResult.Success ->
                                current.copy(
                                    bestGuides = result.data.map { it.toGuideResultUiModel() },
                                    bestGuidesLoadState = ContentLoadState.CONTENT,
                                )
                            is DataResult.Error ->
                                current.copy(
                                    bestGuidesLoadState =
                                        if (hasContent) {
                                            ContentLoadState.CONTENT
                                        } else {
                                            ContentLoadState.ERROR
                                        },
                                )
                        }
                    }
                }
        }

        private companion object {
            const val POPULAR_TOUR_LIMIT = 10
        }
    }
