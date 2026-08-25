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
    val countryCode get() = location.countryCode
    val country get() = location.country
    val cityPlaceId get() = location.cityPlaceId
    val city get() = location.city
    val timeZoneId get() = location.timeZoneId
    val tourDate get() = session.tourDate
    val startTime get() = session.startTime
    val durationMinutes get() = session.durationMinutes
    val price get() = session.price
    val capacity get() = session.capacity
    val meetingPoint get() = session.meetingPoint
    val category get() = content.category
    val spokenLanguages get() = content.spokenLanguages
    val tourName get() = content.tourName
    val tourDescription get() = content.tourDescription
    val selectedCoverImageUri get() = content.selectedCoverImageUri
    val previewImageResId get() = content.previewImageResId
    val guideName get() = guide.name
    val guideImageResId get() = guide.imageResId
    val guideImageUrl get() = guide.imageUrl
    val validationErrorStep get() = submission.validationErrorStep
    val validationErrorResId get() = submission.validationErrorResId
    val isPublishing get() = submission.isPublishing
    val submissionErrorMessage get() = submission.errorMessage
    val publishSucceeded get() = submission.succeeded

    val locationDisplay: String
        get() = listOf(country, city).filter { it.isNotBlank() }.joinToString(", ")

    @StringRes
    fun validationErrorFor(step: GuideTourPublishStep): Int? =
        validationErrorResId.takeIf { validationErrorStep == step }
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
    @param:DrawableRes val previewImageResId: Int = R.drawable.example,
)

data class TourPublishGuideState(
    val name: String = "",
    @param:DrawableRes val imageResId: Int = R.drawable.unnamed,
    val imageUrl: String? = null,
)

data class TourPublishSubmissionState(
    val validationErrorStep: GuideTourPublishStep? = null,
    @param:StringRes val validationErrorResId: Int? = null,
    val isPublishing: Boolean = false,
    val errorMessage: String? = null,
    val succeeded: Boolean = false,
)
