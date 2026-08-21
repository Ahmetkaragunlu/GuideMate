package com.ahmetkaragunlu.guidemate.profile.presentation.guide.model

data class GuideProfileSharedState(
    val profileImageResId: Int,
    val profileImageUrl: String? = null,
    val selectedProfileImageUri: String? = null,
    val title: String,
    val biography: String,
    val spokenLanguages: List<GuideSpokenLanguageUi>,
)
