package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyMinorUnitsOrNull
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishStep
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishUiState
import java.time.Instant

internal data class TourPublishValidationError(
    val step: GuideTourPublishStep,
    @param:StringRes val messageResId: Int,
)

internal fun GuideTourPublishUiState.firstValidationError(): TourPublishValidationError? =
    PUBLISH_INPUT_STEPS.firstNotNullOfOrNull(::findValidationError)

internal fun GuideTourPublishUiState.findValidationError(
    step: GuideTourPublishStep,
): TourPublishValidationError? =
    when (step) {
        GuideTourPublishStep.LOCATION_AND_TIME ->
            if (
                countryCode.isBlank() ||
                    country.isBlank() ||
                    cityPlaceId.isBlank() ||
                    city.isBlank() ||
                    timeZoneId.isBlank() ||
                    toStartInstant()?.isAfter(Instant.now()) != true ||
                    durationMinutes?.let { it > 0 } != true
            ) {
                TourPublishValidationError(step, R.string.error_tour_step1_invalid)
            } else {
                null
            }
        GuideTourPublishStep.TECHNICAL_DETAILS ->
            when {
                spokenLanguages.size > MAX_TOUR_LANGUAGE_COUNT ->
                    TourPublishValidationError(step, R.string.error_tour_languages_too_many)
                category == null ||
                    spokenLanguages.isEmpty() ||
                    price.toCurrencyMinorUnitsOrNull()?.let { it > 0 } != true ||
                    capacity.toIntOrNull()?.let { it > 0 } != true ->
                    TourPublishValidationError(step, R.string.error_tour_step2_invalid)
                else -> null
            }
        GuideTourPublishStep.CONTENT_AND_MEDIA -> contentValidationError()
        GuideTourPublishStep.PREVIEW -> firstValidationError()
    }

private fun GuideTourPublishUiState.contentValidationError(): TourPublishValidationError? {
    val step = GuideTourPublishStep.CONTENT_AND_MEDIA
    val trimmedTitle = tourName.trim()
    val trimmedDescription = tourDescription.trim()
    val trimmedMeetingPoint = meetingPoint.trim()
    return when {
        trimmedTitle.isEmpty() ->
            TourPublishValidationError(step, R.string.error_tour_title_required)
        trimmedTitle.length !in TOUR_TITLE_MIN_LENGTH..TOUR_TITLE_MAX_LENGTH ->
            TourPublishValidationError(step, R.string.error_tour_title_length)
        selectedCoverImageUri == null ->
            TourPublishValidationError(step, R.string.error_tour_cover_required)
        trimmedDescription.isEmpty() ->
            TourPublishValidationError(step, R.string.error_tour_description_required)
        trimmedDescription.length !in TOUR_DESCRIPTION_MIN_LENGTH..TOUR_DESCRIPTION_MAX_LENGTH ->
            TourPublishValidationError(step, R.string.error_tour_description_length)
        trimmedMeetingPoint.isEmpty() ->
            TourPublishValidationError(step, R.string.error_tour_meeting_point_required)
        trimmedMeetingPoint.length > TOUR_MEETING_POINT_MAX_LENGTH ->
            TourPublishValidationError(step, R.string.error_tour_meeting_point_length)
        else -> null
    }
}

internal fun AppError.toTourPublishValidationError(): TourPublishValidationError? {
    val backendError = this as? AppError.Backend ?: return null
    return backendError.fieldErrors.firstNotNullOfOrNull { fieldError ->
        when (fieldError.field.substringAfterLast('.')) {
            "title" ->
                TourPublishValidationError(
                    GuideTourPublishStep.CONTENT_AND_MEDIA,
                    if (fieldError.code == FIELD_REQUIRED_CODE) {
                        R.string.error_tour_title_required
                    } else {
                        R.string.error_tour_title_length
                    },
                )
            "description" ->
                TourPublishValidationError(
                    GuideTourPublishStep.CONTENT_AND_MEDIA,
                    if (fieldError.code == FIELD_REQUIRED_CODE) {
                        R.string.error_tour_description_required
                    } else {
                        R.string.error_tour_description_length
                    },
                )
            "meetingPoint" ->
                TourPublishValidationError(
                    GuideTourPublishStep.CONTENT_AND_MEDIA,
                    if (fieldError.code == FIELD_REQUIRED_CODE) {
                        R.string.error_tour_meeting_point_required
                    } else {
                        R.string.error_tour_meeting_point_length
                    },
                )
            "languageCodes" ->
                TourPublishValidationError(
                    GuideTourPublishStep.TECHNICAL_DETAILS,
                    if (fieldError.code == INVALID_SIZE_CODE) {
                        R.string.error_tour_languages_too_many
                    } else {
                        R.string.error_tour_step2_invalid
                    },
                )
            "countryCode", "cityPlaceId", "cityName", "timeZoneId", "startsAt", "durationMinutes" ->
                TourPublishValidationError(
                    GuideTourPublishStep.LOCATION_AND_TIME,
                    R.string.error_tour_step1_invalid,
                )
            "categoryCode", "priceMinor", "capacity" ->
                TourPublishValidationError(
                    GuideTourPublishStep.TECHNICAL_DETAILS,
                    R.string.error_tour_step2_invalid,
                )
            "coverMediaId" ->
                TourPublishValidationError(
                    GuideTourPublishStep.CONTENT_AND_MEDIA,
                    R.string.error_tour_cover_required,
                )
            else -> null
        }
    }
}

private const val TOUR_TITLE_MIN_LENGTH = 3
private const val TOUR_TITLE_MAX_LENGTH = 120
private const val TOUR_DESCRIPTION_MIN_LENGTH = 20
private const val TOUR_DESCRIPTION_MAX_LENGTH = 3_000
private const val TOUR_MEETING_POINT_MAX_LENGTH = 500
private const val MAX_TOUR_LANGUAGE_COUNT = 20
private const val FIELD_REQUIRED_CODE = "FIELD_REQUIRED"
private const val INVALID_SIZE_CODE = "INVALID_SIZE"

private val PUBLISH_INPUT_STEPS =
    listOf(
        GuideTourPublishStep.LOCATION_AND_TIME,
        GuideTourPublishStep.TECHNICAL_DETAILS,
        GuideTourPublishStep.CONTENT_AND_MEDIA,
    )
