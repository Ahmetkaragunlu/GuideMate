package com.ahmetkaragunlu.guidemate.screens.tourist.explore

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.screens.tourist.explore.model.ExploreTab
import com.ahmetkaragunlu.guidemate.screens.tourist.explore.model.ExploreUiState
import com.ahmetkaragunlu.guidemate.screens.tourist.shared.TourCategoriesData
import com.ahmetkaragunlu.guidemate.screens.tourist.shared.TourCategory

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TouristExploreViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState = _uiState.asStateFlow()

    val categories = TourCategoriesData.categories

    fun updateSelectedTab(tab: ExploreTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun updateSelectedCategory(category: TourCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun updateSelectedRating(rating: Int) {
        _uiState.update { it.copy(selectedRating = rating) }
    }

    fun updatePriceRange(range: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(priceRange = range) }
    }
}