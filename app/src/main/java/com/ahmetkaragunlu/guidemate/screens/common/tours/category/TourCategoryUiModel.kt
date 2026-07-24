package com.ahmetkaragunlu.guidemate.screens.common.tours.category

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class TourCategoryUiModel(
    val category: TourCategory?,
    @param:StringRes val titleResId: Int,
    val icon: ImageVector,
)
