package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import java.time.LocalDate
import java.time.LocalTime

data class GuideTourPublishUiState(
    val location: TourPublishLocationState = TourPublishLocationState(),
    val session: TourPublishSessionFormState = TourPublishSessionFormState(),
    val content: TourPublishContentFormState = TourPublishContentFormState(),
    val guide: TourPublishGuideState = TourPublishGuideState(),
    val submission: TourPublishSubmissionState = TourPublishSubmissionState(),
) {
    val locationDisplay: String
        get() = listOf(location.country, location.city).filter { it.isNotBlank() }.joinToString(", ")

    @StringRes
    fun validationErrorFor(step: GuideTourPublishStep): Int? =
        submission.validationErrorResId.takeIf { submission.validationErrorStep == step }
}

data class TourPublishLocationState(
    val countryCode: String = "",
    val country: String = "",
    val cityPlaceId: String = "",
    val city: String = "",
    val timeZoneId: String = "",
)

data class TourPublishSessionFormState(
    val tourDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val durationMinutes: Int? = null,
    val price: String = "",
    val capacity: String = "",
    val meetingPoint: String = "",
)

data class TourPublishContentFormState(
    val category: TourCategory? = null,
    val spokenLanguages: List<TourLanguage> = emptyList(),
    val tourName: String = "",
    val tourDescription: String = "",
    val selectedCoverImageUri: String? = null,
    @param:DrawableRes val previewImageResId: Int = R.drawable.ic_image_unavailable,
)

data class TourPublishGuideState(
    val name: String = "",
    @param:DrawableRes val imageResId: Int = R.drawable.ic_default_avatar,
    val imageUrl: String? = null,
)

data class TourPublishSubmissionState(
    val validationErrorStep: GuideTourPublishStep? = null,
    @param:StringRes val validationErrorResId: Int? = null,
    val isPublishing: Boolean = false,
    val errorMessage: String? = null,
    val succeeded: Boolean = false,
)
