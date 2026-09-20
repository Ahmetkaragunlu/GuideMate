package com.ahmetkaragunlu.guidemate.tour.presentation.model

data class TourSearchResultUiModel(
    val sessionId: String,
    val title: String,
    val media: TourCardMediaUiModel,
    val rating: TourSearchRatingUiModel?,
    val priceMinor: Long,
    val date: String,
    val location: String,
    val languages: TourCardLanguagesUiModel,
    val availableCapacity: Int,
    val guide: TourCardGuideUiModel,
)

data class TourSearchRatingUiModel(
    val value: Double,
    val reviewCount: Long,
)
