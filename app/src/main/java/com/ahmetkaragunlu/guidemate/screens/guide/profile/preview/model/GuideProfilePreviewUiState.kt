package com.ahmetkaragunlu.guidemate.screens.guide.profile.preview.model

import com.ahmetkaragunlu.guidemate.screens.common.tours.model.PopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideSpokenLanguageUi

data class GuideProfilePreviewUiState(
    val profileImageResId: Int,
    val displayName: String,
    val title: String,
    val guideLevel: String,
    val rating: Double,
    val tourCount: Int,
    val biography: String,
    val spokenLanguages: List<GuideSpokenLanguageUi>,
    val popularTours: List<PopularTourCardUiModel>,
)
