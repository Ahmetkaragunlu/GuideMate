package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish

import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.TourPublishContentFormState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.TourPublishLocationState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.TourPublishSessionFormState
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class GuideTourPublishInputMapperTest {
    @Test
    fun `valid state maps normalized values to create input`() {
        val input = validState().toCreateInputOrNull(coverMediaId = null)

        assertNotNull(input)
        assertEquals("City Walk", input?.content?.title)
        assertEquals("Historic route through the old city", input?.content?.description)
        assertEquals("", input?.content?.coverMediaId)
        assertEquals("Main square", input?.session?.meetingPoint)
        assertEquals(10_050L, input?.session?.priceMinor)
        assertEquals(12, input?.session?.capacity)
        assertEquals(Instant.parse("2099-05-24T09:30:00Z"), input?.session?.startsAt)
    }

    @Test
    fun `invalid state does not create backend input`() {
        val valid = validState()

        assertNull(
            valid
                .copy(session = valid.session.copy(capacity = "0"))
                .toCreateInputOrNull(coverMediaId = null),
        )
    }

    private fun validState() =
        GuideTourPublishUiState(
            location =
                TourPublishLocationState(
                    countryCode = "TR",
                    country = "Turkiye",
                    cityPlaceId = "istanbul-place-id",
                    city = "Istanbul",
                    timeZoneId = "Europe/Istanbul",
                ),
            session =
                TourPublishSessionFormState(
                    tourDate = LocalDate.of(2099, 5, 24),
                    startTime = LocalTime.of(12, 30),
                    durationMinutes = 150,
                    price = "100.50",
                    capacity = "12",
                    meetingPoint = "  Main square  ",
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
                    tourName = "  City Walk  ",
                    tourDescription = "  Historic route through the old city  ",
                    selectedCoverImageUri = "content://cover",
                ),
        )
}
