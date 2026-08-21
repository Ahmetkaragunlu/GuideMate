package com.ahmetkaragunlu.guidemate.profile.presentation.guide.account.about.model

data class GuideAboutUpdatePayload(
    val specialtyTitle: String,
    val biography: String,
    val languageCodes: List<String>,
)

fun GuideAboutUiState.toUpdatePayload(): GuideAboutUpdatePayload =
    GuideAboutUpdatePayload(
        specialtyTitle = specialtyTitle.trim(),
        biography = biography.trim(),
        languageCodes = spokenLanguages.map { it.code },
    )
