package com.ahmetkaragunlu.guidemate.screens.guide.profile.model

data class GuideProfileUiState(
    val profileImageUrl: String? = null,
    val title: String = "",
    val guideLevel: String = "",
    val rating: Double = 0.0,
    val tourCount: Int = 0,
)
