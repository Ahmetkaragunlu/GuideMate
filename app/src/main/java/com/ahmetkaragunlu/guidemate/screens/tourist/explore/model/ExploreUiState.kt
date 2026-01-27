package com.ahmetkaragunlu.guidemate.screens.tourist.explore.model

import com.ahmetkaragunlu.guidemate.screens.tourist.shared.TourCategory


data class ExploreUiState(
    val selectedTab: ExploreTab = ExploreTab.TOURS,
    val selectedCategory: TourCategory = TourCategory.ALL,
    val selectedRating: Int = 0,
    val priceRange: ClosedFloatingPointRange<Float> = 0f..500f
)