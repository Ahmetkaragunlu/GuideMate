package com.ahmetkaragunlu.guidemate.profile.presentation.mapper

import com.ahmetkaragunlu.guidemate.common.location.data.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfile
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideProfileContentUiState
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideSpokenLanguageUi
import com.ahmetkaragunlu.guidemate.tour.presentation.model.PopularTourCardUiModel
import java.util.Locale

fun GuideProfile?.toProfileContentUiState(
    loadState: ContentLoadState,
    popularTours: List<PopularTourCardUiModel> = emptyList(),
): GuideProfileContentUiState {
    val locale = Locale.getDefault()
    return GuideProfileContentUiState(
        profileImageUrl = this?.avatar?.imageUrl,
        displayName = this?.displayName.orEmpty(),
        title = this?.specialtyTitle.orEmpty(),
        guideLevel = this?.performance?.level ?: GuideLevelTier.APPROVED,
        rating = this?.performance?.averageRating ?: 0.0,
        tourCount = this?.performance?.completedSessionCount ?: 0L,
        biography = this?.biography.orEmpty(),
        spokenLanguages =
            this?.languageCodes.orEmpty().map { code ->
                GuideSpokenLanguageUi(
                    code = code,
                    displayText =
                        LocaleSelectionCatalog.language(code, locale)?.chipLabel
                            ?: code.uppercase(locale),
                )
            },
        popularTours = popularTours,
        loadState = loadState,
    )
}
