package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model

import androidx.annotation.DrawableRes
import com.ahmetkaragunlu.guidemate.R

data class GuideTourPublishUiState(
    val country: String = "",
    val city: String = "",
    val tourDate: String = "",
    val category: String = "",
    val spokenLanguages: List<GuideTourLanguageUi> = emptyList(),
    val price: String = "",
    val tourName: String = "",
    val tourDescription: String = "",
    val meetingPoint: String = "",
    @DrawableRes val previewImageResId: Int = R.drawable.example,
) {
    val locationDisplay: String
        get() = listOf(country, city).filter { it.isNotBlank() }.joinToString(", ")
}

