package com.ahmetkaragunlu.guidemate.screens.guide.profile.model

import com.ahmetkaragunlu.guidemate.screens.common.tours.model.PopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.profile.guidelevel.model.GuideLevelTier

data class GuideProfileUiState(
    val firstName: String? = null,
    val lastName: String? = null,
    val profileImageResId: Int? = null,
    val profileImageUrl: String? = null,
    val title: String = "",
    val guideLevel: GuideLevelTier = GuideLevelTier.APPROVED,
    val rating: Double = 0.0,
    val tourCount: Int = 0,
    val biography: String = "",
    val spokenLanguages: List<GuideSpokenLanguageUi> = emptyList(),
    val popularTours: List<PopularTourCardUiModel> = emptyList(),
) {
    val displayName: String
        get() =
            listOfNotNull(
                firstName?.takeIf { it.isNotBlank() },
                lastName?.takeIf { it.isNotBlank() },
            ).joinToString(" ").ifBlank { "Ahmet Karagünlü" }
}
