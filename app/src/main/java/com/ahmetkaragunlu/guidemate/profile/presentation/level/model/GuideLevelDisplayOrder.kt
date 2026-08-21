package com.ahmetkaragunlu.guidemate.profile.presentation.level.model

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier

val GUIDE_LEVEL_DISPLAY_ORDER: List<GuideLevelTier> =
    GuideLevelTier.entries

@get:StringRes
val GuideLevelTier.iconResId: Int
    get() =
        when (this) {
            GuideLevelTier.APPROVED -> R.string.guide_level_approved_icon
            GuideLevelTier.SILVER -> R.string.guide_level_silver_icon
            GuideLevelTier.SUPER -> R.string.guide_level_super_icon
            GuideLevelTier.LEGENDARY -> R.string.guide_level_legendary_icon
        }

@get:StringRes
val GuideLevelTier.titleResId: Int
    get() =
        when (this) {
            GuideLevelTier.APPROVED -> R.string.guide_level_approved_title
            GuideLevelTier.SILVER -> R.string.guide_level_silver_title
            GuideLevelTier.SUPER -> R.string.guide_level_super_title
            GuideLevelTier.LEGENDARY -> R.string.guide_level_legendary_title
        }

@get:StringRes
val GuideLevelTier.descriptionResId: Int
    get() =
        when (this) {
            GuideLevelTier.APPROVED -> R.string.guide_level_approved_desc
            GuideLevelTier.SILVER -> R.string.guide_level_silver_desc
            GuideLevelTier.SUPER -> R.string.guide_level_super_desc
            GuideLevelTier.LEGENDARY -> R.string.guide_level_legendary_desc
        }
