package com.ahmetkaragunlu.guidemate.profile.presentation.guide.preview

import com.ahmetkaragunlu.guidemate.tour.presentation.model.PopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.model.GuideSpokenLanguageUi

data class GuideProfilePreviewUiState(
    val profileImageResId: Int,
    val profileImageUrl: String?,
    val displayName: String,
    val title: String,
    val guideLevel: GuideLevelTier,
    val rating: Double,
    val tourCount: Int,
    val biography: String,
    val spokenLanguages: List<GuideSpokenLanguageUi>,
    val popularTours: List<PopularTourCardUiModel>,
)
