package com.ahmetkaragunlu.guidemate.screens.tourist.trips

import androidx.annotation.DrawableRes
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategory

data class TripUiModel(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    @param:DrawableRes val imageResId: Int,
    val participantCount: Int,
    val category: TourCategory,
    val languagesFlag: String,
    val languagesText: String,
    val price: Double,
    val rating: Double? = null,
    val reviewCount: Int? = null,
    val isPast: Boolean = false,
)
