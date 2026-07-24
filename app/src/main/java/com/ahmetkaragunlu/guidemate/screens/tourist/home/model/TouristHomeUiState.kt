package com.ahmetkaragunlu.guidemate.screens.tourist.home.model

import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategory
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.PopularTourCardUiModel

data class TouristHomeUiState(
    val selectedCategory: TourCategory? = null,
    val popularTours: List<PopularTourCardUiModel> = emptyList(),
    val bestGuides: List<BestGuideUiModel> = emptyList(),
)
