package com.ahmetkaragunlu.guidemate.tour.presentation.category

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory

data class TourCategoryUiModel(
    val category: TourCategory?,
    @param:StringRes val titleResId: Int,
    val icon: ImageVector,
)
