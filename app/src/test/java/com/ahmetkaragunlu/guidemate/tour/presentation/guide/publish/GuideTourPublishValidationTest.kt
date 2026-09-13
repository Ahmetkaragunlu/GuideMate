package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.AppFieldError
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishStep
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.TourPublishContentFormState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.TourPublishLocationState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.TourPublishSessionFormState
import java.time.LocalDate
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GuideTourPublishValidationTest {
    @Test
    fun `missing location and time returns first step error`() {
        val error = GuideTourPublishUiState().firstValidationError()

        assertEquals(GuideTourPublishStep.LOCATION_AND_TIME, error?.step)
        assertEquals(R.string.error_tour_step1_invalid, error?.messageResId)
    }

    @Test
    fun `invalid price or capacity returns technical details error`() {
        val valid = validState()
        val state = valid.copy(session = valid.session.copy(price = "0"))

        val error = state.findValidationError(GuideTourPublishStep.TECHNICAL_DETAILS)

        assertEquals(GuideTourPublishStep.TECHNICAL_DETAILS, error?.step)
        assertEquals(R.string.error_tour_step2_invalid, error?.messageResId)
    }

    @Test
    fun `content validation reports title description cover and meeting point rules`() {
        val valid = validState()

        assertEquals(
            R.string.error_tour_title_length,
            valid
                .copy(content = valid.content.copy(tourName = "AB"))
                .findValidationError(GuideTourPublishStep.CONTENT_AND_MEDIA)
                ?.messageResId,
        )
        assertEquals(
            R.string.error_tour_cover_required,
            valid
                .copy(content = valid.content.copy(selectedCoverImageUri = null))
                .findValidationError(GuideTourPublishStep.CONTENT_AND_MEDIA)
                ?.messageResId,
        )
        assertEquals(
            R.string.error_tour_description_length,
            valid
                .copy(content = valid.content.copy(tourDescription = "Short"))
                .findValidationError(GuideTourPublishStep.CONTENT_AND_MEDIA)
                ?.messageResId,
        )
        assertEquals(
            R.string.error_tour_meeting_point_required,
            valid
                .copy(session = valid.session.copy(meetingPoint = " "))
                .findValidationError(GuideTourPublishStep.CONTENT_AND_MEDIA)
                ?.messageResId,
        )
        assertNull(valid.firstValidationError())
    }

    @Test
    fun `backend field error maps to owning publish step`() {
        val error =
            AppError.Backend(
                code = null,
                fallbackMessage = null,
                fieldErrors =
                    listOf(
                        AppFieldError(
                            field = "content.languageCodes",
                            code = "INVALID_SIZE",
                            fallbackMessage = null,
                        ),
                    ),
            )

        val validationError = error.toTourPublishValidationError()

        assertEquals(GuideTourPublishStep.TECHNICAL_DETAILS, validationError?.step)
        assertEquals(R.string.error_tour_languages_too_many, validationError?.messageResId)
    }

    private fun validState() =
        GuideTourPublishUiState(
            location =
                TourPublishLocationState(
                    countryCode = "TR",
                    country = "Turkiye",
                    cityPlaceId = "istanbul-place-id",
                    city = "Istanbul",
                    timeZoneId = "UTC",
                ),
            session =
                TourPublishSessionFormState(
                    tourDate = LocalDate.of(2099, 1, 1),
                    startTime = LocalTime.of(12, 0),
                    durationMinutes = 120,
                    price = "100",
                    capacity = "10",
                    meetingPoint = "Main square",
                ),
            content =
                TourPublishContentFormState(
                    category = TourCategory.CULTURE,
                    spokenLanguages =
                        listOf(
                            TourLanguage(
                                code = "en",
                                flagEmoji = "",
                                displayName = "English",
                                shortCode = "EN",
                            ),
                        ),
                    tourName = "City Walk",
                    tourDescription = "Historic route through the old city",
                    selectedCoverImageUri = "content://cover",
                ),
        )
}
