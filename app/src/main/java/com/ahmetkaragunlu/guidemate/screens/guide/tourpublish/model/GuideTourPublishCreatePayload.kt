package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model

data class GuideTourPublishCreatePayload(
    val country: String,
    val city: String,
    val tourDate: String,
    val category: String,
    val languageCodes: List<String>,
    val price: Int?,
    val tourName: String,
    val tourDescription: String,
    val meetingPoint: String,
    val mediaAssetIds: List<String> = emptyList(),
)

fun GuideTourPublishUiState.toCreatePayload(): GuideTourPublishCreatePayload =
    GuideTourPublishCreatePayload(
        country = country.trim(),
        city = city.trim(),
        tourDate = tourDate.trim(),
        category = category.trim(),
        languageCodes = spokenLanguages.map { it.code },
        price = price.toIntOrNull(),
        tourName = tourName.trim(),
        tourDescription = tourDescription.trim(),
        meetingPoint = meetingPoint.trim(),
    )

