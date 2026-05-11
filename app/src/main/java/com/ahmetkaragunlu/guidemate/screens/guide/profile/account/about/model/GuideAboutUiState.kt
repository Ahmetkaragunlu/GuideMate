package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.model

import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideSpokenLanguageUi

data class GuideAboutUiState(
    val specialtyTitle: String = "",
    val biography: String = "",
    val spokenLanguages: List<GuideSpokenLanguageUi> = emptyList(),
    val isSaving: Boolean = false,
)
