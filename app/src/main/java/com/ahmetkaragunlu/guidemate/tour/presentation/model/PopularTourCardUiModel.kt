package com.ahmetkaragunlu.guidemate.tour.presentation.model

data class PopularTourCardUiModel(
    val id: String,
    val title: String,
    val media: TourCardMediaUiModel,
    val rating: PopularTourRatingUiModel,
    val priceMinor: Long,
    val languages: TourCardLanguagesUiModel,
    val guide: TourCardGuideUiModel,
)

data class PopularTourRatingUiModel(
    val value: String,
    val reviewCount: String,
)
