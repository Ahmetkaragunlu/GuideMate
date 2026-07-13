package com.ahmetkaragunlu.guidemate.screens.common.tours.model

import androidx.annotation.DrawableRes

data class PopularTourCardUiModel(
    val id: String,
    val title: String,
    @param:DrawableRes val imageResId: Int,
    val rating: String,
    val reviewCount: String,
    val price: Double,
    val languagesFlag: String,
    val languagesText: String,
    val guideName: String,
    @param:DrawableRes val guideImageResId: Int,
)
