package com.ahmetkaragunlu.guidemate.profile.domain.model.level

enum class GuideLevelTier(
    val minTourCount: Int,
    val minRating: Double,
    val minReviewCount: Int,
) {
    APPROVED(
        minTourCount = 0,
        minRating = 0.0,
        minReviewCount = 0,
    ),
    SILVER(
        minTourCount = 5,
        minRating = 3.7,
        minReviewCount = 3,
    ),
    SUPER(
        minTourCount = 20,
        minRating = 4.5,
        minReviewCount = 10,
    ),
    LEGENDARY(
        minTourCount = 100,
        minRating = 4.8,
        minReviewCount = 30,
    ),
}
