package com.ahmetkaragunlu.guidemate.tour.presentation.model

import androidx.annotation.DrawableRes

data class TourSearchResultUiModel(
    val sessionId: String,
    val title: String,
    @param:DrawableRes val imageResId: Int,
    val imageUrl: String?,
    val rating: Double?,
    val reviewCount: Long,
    val priceMinor: Long,
    val date: String,
    val location: String,
    val languagesFlag: String,
    val languagesText: String,
    val availableCapacity: Int,
    val guideName: String,
    @param:DrawableRes val guideImageResId: Int,
    val guideImageUrl: String?,
)
