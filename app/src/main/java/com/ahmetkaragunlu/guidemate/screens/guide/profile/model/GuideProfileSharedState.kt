package com.ahmetkaragunlu.guidemate.screens.guide.profile.model

import com.ahmetkaragunlu.guidemate.screens.common.tours.model.PopularTourCardUiModel

data class GuideProfileSharedState(
    val profileImageResId: Int,
    val profileImageUrl: String? = null,
    val title: String,
    val rating: Double,
    val tourCount: Int,
    val biography: String,
    val spokenLanguages: List<GuideSpokenLanguageUi>,
    val popularTours: List<PopularTourCardUiModel>,
)
