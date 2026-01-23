package com.ahmetkaragunlu.guidemate.screens.tourist.home

data class PopularToursCardUiModel(
    val id: String,
    val title: String,
    val imageUrl: Int,
    val rating: String,
    val reviewCount: String,
    val price: String,
    val languagesFlag: String,
    val languagesText: String,
    val guideName: String,
    val guideImageUrl: Int
)