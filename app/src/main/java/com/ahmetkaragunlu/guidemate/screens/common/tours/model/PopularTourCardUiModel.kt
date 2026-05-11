package com.ahmetkaragunlu.guidemate.screens.common.tours.model

data class PopularTourCardUiModel(
    val id: String,
    val title: String,
    val imageUrl: Int,
    val rating: String,
    val reviewCount: String,
    val price: Double,
    val languagesFlag: String,
    val languagesText: String,
    val guideName: String,
    val guideImageUrl: Int,
)
