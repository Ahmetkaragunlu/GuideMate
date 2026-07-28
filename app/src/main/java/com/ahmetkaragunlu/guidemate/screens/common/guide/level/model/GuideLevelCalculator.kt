package com.ahmetkaragunlu.guidemate.screens.common.guide.level.model

fun calculateGuideLevelTier(
    completedSessionCount: Int,
    rating: Double,
    reviewCount: Int,
): GuideLevelTier =
    when {
        completedSessionCount >= GuideLevelTier.LEGENDARY.minTourCount &&
            rating >= GuideLevelTier.LEGENDARY.minRating &&
            reviewCount >= GuideLevelTier.LEGENDARY.minReviewCount -> GuideLevelTier.LEGENDARY
        completedSessionCount >= GuideLevelTier.SUPER.minTourCount &&
            rating >= GuideLevelTier.SUPER.minRating &&
            reviewCount >= GuideLevelTier.SUPER.minReviewCount -> GuideLevelTier.SUPER
        completedSessionCount >= GuideLevelTier.SILVER.minTourCount &&
            rating >= GuideLevelTier.SILVER.minRating &&
            reviewCount >= GuideLevelTier.SILVER.minReviewCount -> GuideLevelTier.SILVER
        else -> GuideLevelTier.APPROVED
    }
