package com.ahmetkaragunlu.guidemate.home.presentation.tourist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.profile.presentation.mapper.toGuideResultUiModel
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideResultUiModel
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.category.TourCategoryCatalog
import com.ahmetkaragunlu.guidemate.tour.presentation.mapper.toPopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.tour.data.mock.TourCatalogStore
import com.ahmetkaragunlu.guidemate.tour.data.mock.refreshAtSessionTransitions
import com.ahmetkaragunlu.guidemate.home.presentation.tourist.model.TouristHomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TouristHomeViewModel @Inject constructor(
    userRepository: UserRepository,
    tourCatalogStore: TourCatalogStore,
    private val profileRepository: GuideProfileRepository,
) : ViewModel() {

    val userName: StateFlow<String?> =
        userRepository.userState
            .map { it.firstName }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null,
            )

    val categories = TourCategoryCatalog.filterOptions

    private val _selectedCategory = MutableStateFlow<TourCategory?>(null)
    private val bestGuides = MutableStateFlow<List<GuideResultUiModel>>(emptyList())
    private val bestGuidesLoadState = MutableStateFlow(ContentLoadState.LOADING)
    private var bestGuidesJob: Job? = null

    fun updateSelectedCategory(category: TourCategory?) {
        _selectedCategory.value = category
    }

    val uiState: StateFlow<TouristHomeUiState> =
        combine(
            _selectedCategory,
            tourCatalogStore.state.refreshAtSessionTransitions(),
            bestGuides,
            bestGuidesLoadState,
        ) { selectedCategory, catalog, guides, guidesLoadState ->
                val now = Instant.now()
                val popularTours =
                    catalog
                        .bookableTourItemsAt(now)
                        .filter { selectedCategory == null || it.tour.category == selectedCategory }
                        .map { it.toPopularTourCardUiModel() }
                TouristHomeUiState(
                    selectedCategory = selectedCategory,
                    popularTours = popularTours,
                    bestGuides = guides,
                    bestGuidesLoadState = guidesLoadState,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue =
                    TouristHomeUiState(
                        selectedCategory = null,
                        popularTours =
                            tourCatalogStore.state.value
                                .bookableTourItemsAt(Instant.now())
                                .map {
                                    it.toPopularTourCardUiModel()
                                },
                        bestGuides = emptyList(),
                    ),
            )

    init {
        refreshBestGuides()
    }

    fun refreshBestGuides() {
        if (bestGuidesJob?.isActive == true) return
        bestGuidesJob =
            viewModelScope.launch {
                val hasContent = bestGuides.value.isNotEmpty()
                if (!hasContent) bestGuidesLoadState.value = ContentLoadState.LOADING
                when (val result = profileRepository.getTopGuides(limit = 4)) {
                    is DataResult.Success -> {
                        bestGuides.value = result.data.map { it.toGuideResultUiModel() }
                        bestGuidesLoadState.value = ContentLoadState.CONTENT
                    }
                    is DataResult.Error -> {
                        bestGuidesLoadState.value =
                            if (hasContent) ContentLoadState.CONTENT else ContentLoadState.ERROR
                    }
                }
            }
    }
}
