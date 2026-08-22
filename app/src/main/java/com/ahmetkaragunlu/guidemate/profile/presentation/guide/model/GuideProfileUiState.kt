package com.ahmetkaragunlu.guidemate.profile.presentation.guide.model

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideSpokenLanguageUi
import com.ahmetkaragunlu.guidemate.tour.presentation.model.PopularTourCardUiModel

data class GuideProfileUiState(
    val guideId: Long? = null,
    val displayName: String = "",
    val profileImageResId: Int = R.drawable.unnamed,
    val profileImageUrl: String? = null,
    val selectedProfileImageUri: String? = null,
    val title: String = "",
    val guideLevel: GuideLevelTier = GuideLevelTier.APPROVED,
    val rating: Double = 0.0,
    val tourCount: Long = 0,
    val biography: String = "",
    val spokenLanguages: List<GuideSpokenLanguageUi> = emptyList(),
    val popularTours: List<PopularTourCardUiModel> = emptyList(),
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val isAvatarUpdating: Boolean = false,
    val userMessage: String? = null,
) {
    val displayProfileImageUrl: String?
        get() = selectedProfileImageUri ?: profileImageUrl
}
