package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.model

data class GuideAboutUiState(
    val specialtyTitle: String = "",
    val biography: String = "",
    val spokenLanguages: List<GuideAboutLanguageUi> = emptyList(),
    val isSaving: Boolean = false,
)
