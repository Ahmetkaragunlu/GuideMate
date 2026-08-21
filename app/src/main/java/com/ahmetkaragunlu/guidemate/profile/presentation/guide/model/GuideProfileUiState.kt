package com.ahmetkaragunlu.guidemate.profile.presentation.guide.model

import com.ahmetkaragunlu.guidemate.tour.presentation.model.PopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier

data class GuideProfileUiState(
    val firstName: String? = null,
    val lastName: String? = null,
    val profileImageResId: Int? = null,
    val profileImageUrl: String? = null,
    val selectedProfileImageUri: String? = null,
    val title: String = "",
    val guideLevel: GuideLevelTier = GuideLevelTier.APPROVED,
    val rating: Double = 0.0,
    val tourCount: Int = 0,
    val biography: String = "",
    val spokenLanguages: List<GuideSpokenLanguageUi> = emptyList(),
    val popularTours: List<PopularTourCardUiModel> = emptyList(),
) {
    val displayProfileImageUrl: String?
        get() = selectedProfileImageUri ?: profileImageUrl

    val displayName: String
        get() =
            listOfNotNull(
                firstName?.takeIf { it.isNotBlank() },
                lastName?.takeIf { it.isNotBlank() },
            ).joinToString(" ").ifBlank { "Ahmet Karagünlü" }
}
