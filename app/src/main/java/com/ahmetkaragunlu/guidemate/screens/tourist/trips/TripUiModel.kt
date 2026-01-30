package com.ahmetkaragunlu.guidemate.screens.tourist.trips

data class TripUiModel(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    val imageUrl: Int,
    val isPast: Boolean = false
)
