package com.ahmetkaragunlu.guidemate.screens.tourist.home

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryItem(
    val id: Int,
    @StringRes val titleResId: Int,
    val icon: ImageVector
)