package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model

data class GuideTourLanguageUi(
    val code: String,
    val flagEmoji: String,
    val displayName: String,
    val previewCode: String,
) {
    val chipLabel: String
        get() = "$flagEmoji $displayName"
}

