package com.ahmetkaragunlu.guidemate.screens.guide.profile.preview.model

import com.ahmetkaragunlu.guidemate.screens.common.tours.model.PopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.profile.guidelevel.model.GuideLevelTier
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideSpokenLanguageUi

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
