package com.ahmetkaragunlu.guidemate.home.presentation.tourist.model

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideResultUiModel
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.model.PopularTourCardUiModel

data class TouristHomeUiState(
    val selectedCategory: TourCategory? = null,
    val popularTours: List<PopularTourCardUiModel> = emptyList(),
    val bestGuides: List<GuideResultUiModel> = emptyList(),
    val bestGuidesLoadState: ContentLoadState = ContentLoadState.LOADING,
)
