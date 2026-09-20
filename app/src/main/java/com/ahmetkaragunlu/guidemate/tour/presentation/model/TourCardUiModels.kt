package com.ahmetkaragunlu.guidemate.tour.presentation.model

import androidx.annotation.DrawableRes

data class TourCardMediaUiModel(
    @param:DrawableRes val imageResId: Int,
    val imageUrl: String?,
)

data class TourCardLanguagesUiModel(
    val flags: String,
    val shortCodes: String,
)

data class TourCardGuideUiModel(
    val name: String,
    @param:DrawableRes val imageResId: Int,
    val imageUrl: String?,
)
