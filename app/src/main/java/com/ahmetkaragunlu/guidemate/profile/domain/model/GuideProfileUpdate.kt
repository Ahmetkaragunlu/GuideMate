package com.ahmetkaragunlu.guidemate.profile.domain.model

data class GuideProfileUpdate(
    val specialtyTitle: String,
    val biography: String,
    val languageCodes: List<String>,
) {
    companion object {
        const val MIN_SPECIALTY_TITLE_LENGTH = 2
        const val MAX_SPECIALTY_TITLE_LENGTH = 60
        const val MIN_BIOGRAPHY_LENGTH = 20
        const val MAX_BIOGRAPHY_LENGTH = 1_000
    }
}
