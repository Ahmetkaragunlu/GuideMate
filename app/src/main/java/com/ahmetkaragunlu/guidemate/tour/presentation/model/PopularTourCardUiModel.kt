package com.ahmetkaragunlu.guidemate.tour.presentation.model

import androidx.annotation.DrawableRes

data class PopularTourCardUiModel(
    val id: String,
    val title: String,
    @param:DrawableRes val imageResId: Int,
    val imageUrl: String? = null,
    val rating: String,
    val reviewCount: String,
    val priceMinor: Long,
    val languagesFlag: String,
    val languagesText: String,
    val guideName: String,
    @param:DrawableRes val guideImageResId: Int,
    val guideImageUrl: String? = null,
)
