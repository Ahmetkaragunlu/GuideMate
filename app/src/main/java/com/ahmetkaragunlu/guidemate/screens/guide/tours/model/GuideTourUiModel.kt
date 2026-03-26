package com.ahmetkaragunlu.guidemate.screens.guide.tours.model

data class GuideTourUiModel(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    val imageUrl: Int,
    val participantCount: Int,
    val languagesFlag: String,
    val languagesText: String,
    val category: String,
    val price: Double,
    val rating: Double?,
    val reviewCount: Int?,
    val isActive: Boolean = true,
    val isLive: Boolean = false,
    val earnings: Double? = null,
)
