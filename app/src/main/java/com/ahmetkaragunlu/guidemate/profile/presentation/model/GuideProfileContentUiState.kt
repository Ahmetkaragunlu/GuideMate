package com.ahmetkaragunlu.guidemate.profile.presentation.model

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.tour.presentation.model.PopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier

data class GuideProfileContentUiState(
    val profileImageResId: Int = R.drawable.ic_default_avatar,
    val profileImageUrl: String? = null,
    val displayName: String = "",
    val title: String = "",
    val guideLevel: GuideLevelTier = GuideLevelTier.APPROVED,
    val rating: Double = 0.0,
    val tourCount: Long = 0,
    val biography: String = "",
    val spokenLanguages: List<GuideSpokenLanguageUi> = emptyList(),
    val popularTours: List<PopularTourCardUiModel> = emptyList(),
    val loadState: ContentLoadState = ContentLoadState.LOADING,
)
