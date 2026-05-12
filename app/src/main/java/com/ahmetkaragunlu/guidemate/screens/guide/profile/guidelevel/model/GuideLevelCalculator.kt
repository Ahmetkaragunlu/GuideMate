package com.ahmetkaragunlu.guidemate.screens.guide.profile.guidelevel.model

fun calculateGuideLevelTier(
    tourCount: Int,
    rating: Double,
): GuideLevelTier =
    when {
        tourCount >= GuideLevelTier.LEGENDARY.minTourCount &&
            rating >= GuideLevelTier.LEGENDARY.minRating -> GuideLevelTier.LEGENDARY
        tourCount >= GuideLevelTier.SUPER.minTourCount &&
            rating >= GuideLevelTier.SUPER.minRating -> GuideLevelTier.SUPER
        tourCount >= GuideLevelTier.SILVER.minTourCount &&
            rating >= GuideLevelTier.SILVER.minRating -> GuideLevelTier.SILVER
        else -> GuideLevelTier.APPROVED
    }
