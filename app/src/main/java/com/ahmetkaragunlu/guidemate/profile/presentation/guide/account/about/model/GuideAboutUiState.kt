package com.ahmetkaragunlu.guidemate.profile.presentation.guide.account.about.model

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfileUpdate
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideSpokenLanguageUi

data class GuideAboutUiState(
    val specialtyTitle: String = "",
    val biography: String = "",
    val spokenLanguages: List<GuideSpokenLanguageUi> = emptyList(),
    val showValidationErrors: Boolean = false,
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val saveCompleted: Boolean = false,
) {
    val isSpecialtyTitleValid: Boolean
        get() = specialtyTitle.trim().length in MIN_SPECIALTY_TITLE_LENGTH..MAX_SPECIALTY_TITLE_LENGTH

    val isBiographyValid: Boolean
        get() = biography.trim().length in MIN_BIOGRAPHY_LENGTH..MAX_BIOGRAPHY_LENGTH

    val isFormValid: Boolean
        get() = isSpecialtyTitleValid && isBiographyValid

    companion object {
        const val MIN_SPECIALTY_TITLE_LENGTH = GuideProfileUpdate.MIN_SPECIALTY_TITLE_LENGTH
        const val MAX_SPECIALTY_TITLE_LENGTH = GuideProfileUpdate.MAX_SPECIALTY_TITLE_LENGTH
        const val MIN_BIOGRAPHY_LENGTH = GuideProfileUpdate.MIN_BIOGRAPHY_LENGTH
        const val MAX_BIOGRAPHY_LENGTH = GuideProfileUpdate.MAX_BIOGRAPHY_LENGTH
    }
}
