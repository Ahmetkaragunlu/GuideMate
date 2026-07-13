package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.model

import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideSpokenLanguageUi

data class GuideAboutUiState(
    val specialtyTitle: String = "",
    val biography: String = "",
    val spokenLanguages: List<GuideSpokenLanguageUi> = emptyList(),
    val showValidationErrors: Boolean = false,
) {
    val isSpecialtyTitleValid: Boolean
        get() = specialtyTitle.trim().length in MIN_SPECIALTY_TITLE_LENGTH..MAX_SPECIALTY_TITLE_LENGTH

    val isBiographyValid: Boolean
        get() = biography.trim().length in MIN_BIOGRAPHY_LENGTH..MAX_BIOGRAPHY_LENGTH

    val isFormValid: Boolean
        get() = isSpecialtyTitleValid && isBiographyValid

    companion object {
        const val MIN_SPECIALTY_TITLE_LENGTH = 2
        const val MAX_SPECIALTY_TITLE_LENGTH = 60
        const val MIN_BIOGRAPHY_LENGTH = 20
        const val MAX_BIOGRAPHY_LENGTH = 1_000
    }
}
