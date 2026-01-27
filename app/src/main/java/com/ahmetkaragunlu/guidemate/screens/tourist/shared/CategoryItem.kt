package com.ahmetkaragunlu.guidemate.screens.tourist.shared

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryItem(
    val type: TourCategory,
    @StringRes val titleResId: Int,
    val icon: ImageVector
)