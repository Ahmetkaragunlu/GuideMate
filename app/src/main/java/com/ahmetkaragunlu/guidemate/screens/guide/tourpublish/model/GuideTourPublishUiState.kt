package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategory
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourLanguage
import java.time.LocalDate
import java.time.LocalTime

data class GuideTourPublishUiState(
    val countryCode: String = "",
    val country: String = "",
    val cityPlaceId: String = "",
    val city: String = "",
    val timeZoneId: String = "",
    val tourDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val durationMinutes: Int? = null,
    val category: TourCategory? = null,
    val spokenLanguages: List<TourLanguage> = emptyList(),
    val price: String = "",
    val capacity: String = "",
    val tourName: String = "",
    val tourDescription: String = "",
    val meetingPoint: String = "",
    val selectedCoverImageUri: String? = null,
    @param:DrawableRes val previewImageResId: Int = R.drawable.example,
    val guideName: String = "",
    @param:DrawableRes val guideImageResId: Int = R.drawable.unnamed,
    val validationErrorStep: GuideTourPublishStep? = null,
    @param:StringRes val validationErrorResId: Int? = null,
) {
    val locationDisplay: String
        get() = listOf(country, city).filter { it.isNotBlank() }.joinToString(", ")

    @StringRes
    fun validationErrorFor(step: GuideTourPublishStep): Int? =
        validationErrorResId.takeIf { validationErrorStep == step }
}
