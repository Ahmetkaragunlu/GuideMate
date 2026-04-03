package com.ahmetkaragunlu.guidemate.screens.tourist.home.model

import com.ahmetkaragunlu.guidemate.screens.tourist.shared.TourCategory

data class TouristHomeUiState(
    val selectedCategory: TourCategory = TourCategory.ALL,
    val popularTours: List<PopularToursCardUiModel> = emptyList(),
    val bestGuides: List<BestGuideUiModel> = emptyList(),
)
