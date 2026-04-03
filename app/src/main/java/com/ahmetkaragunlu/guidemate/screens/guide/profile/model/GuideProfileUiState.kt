package com.ahmetkaragunlu.guidemate.screens.guide.profile.model

data class GuideProfileUiState(
    val firstName: String? = null,
    val lastName: String? = null,
    val profileImageUrl: String? = null,
    val title: String = "",
    val guideLevel: String = "",
    val rating: Double = 0.0,
    val tourCount: Int = 0,
) {
    val displayName: String
        get() =
            listOfNotNull(
                firstName?.takeIf { it.isNotBlank() },
                lastName?.takeIf { it.isNotBlank() },
            ).joinToString(" ").ifBlank { "Ahmet Karagünlü" }
}
