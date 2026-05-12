package com.ahmetkaragunlu.guidemate.screens.guide.profile.guidelevel.model

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R

enum class GuideLevelTier(
    @StringRes val iconResId: Int,
    @StringRes val titleResId: Int,
    @StringRes val descriptionResId: Int,
    val minTourCount: Int,
    val minRating: Double,
) {
    APPROVED(
        iconResId = R.string.guide_level_approved_icon,
        titleResId = R.string.guide_level_approved_title,
        descriptionResId = R.string.guide_level_approved_desc,
        minTourCount = 0,
        minRating = 0.0,
    ),
    SILVER(
        iconResId = R.string.guide_level_silver_icon,
        titleResId = R.string.guide_level_silver_title,
        descriptionResId = R.string.guide_level_silver_desc,
        minTourCount = 5,
        minRating = 3.7,
    ),
    SUPER(
        iconResId = R.string.guide_level_super_icon,
        titleResId = R.string.guide_level_super_title,
        descriptionResId = R.string.guide_level_super_desc,
        minTourCount = 20,
        minRating = 4.5,
    ),
    LEGENDARY(
        iconResId = R.string.guide_level_legendary_icon,
        titleResId = R.string.guide_level_legendary_title,
        descriptionResId = R.string.guide_level_legendary_desc,
        minTourCount = 100,
        minRating = 4.8,
    ),
}
