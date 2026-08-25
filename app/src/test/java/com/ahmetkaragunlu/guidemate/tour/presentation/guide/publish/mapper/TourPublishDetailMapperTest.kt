package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.mapper

import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.TourPublishContentFormState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.TourPublishGuideState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.TourPublishLocationState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.TourPublishSessionFormState
import java.time.LocalDate
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TourPublishDetailMapperTest {
    @Test
    fun `preview uses only guide entered publish values`() {
        val state =
            GuideTourPublishUiState(
                location =
                    TourPublishLocationState(
                        countryCode = "TR",
                        country = "Türkiye",
                        cityPlaceId = "istanbul",
                        city = "İstanbul",
                        timeZoneId = "Europe/Istanbul",
                    ),
                session =
                    TourPublishSessionFormState(
                        tourDate = LocalDate.of(2026, 9, 1),
                        startTime = LocalTime.of(10, 30),
                        durationMinutes = 120,
                        price = "1500.25",
                        capacity = "12",
                        meetingPoint = "Sultanahmet Square",
                    ),
                content =
                    TourPublishContentFormState(
                        category = TourCategory.CULTURE,
                        spokenLanguages =
                            listOf(
                                TourLanguage(
                                    code = "en",
                                    flagEmoji = "🇬🇧",
                                    displayName = "English",
                                    shortCode = "EN",
                                ),
                            ),
                        tourName = "Historic Istanbul",
                        tourDescription = "A guided city walk",
                        selectedCoverImageUri = "content://cover",
                    ),
                guide =
                    TourPublishGuideState(
                        name = "Ada Guide",
                        imageUrl = "https://example.com/avatar.jpg",
                    ),
            )

        val preview = state.toPreviewDetailUiState()

        assertEquals("Historic Istanbul", preview.title)
        assertEquals("A guided city walk", preview.description)
        assertEquals("Sultanahmet Square", preview.meetingPoint)
        assertEquals("Türkiye, İstanbul", preview.location)
        assertEquals(150_025L, preview.priceMinor)
        assertEquals(12, preview.capacity)
        assertEquals("Ada Guide", preview.guideName)
        assertEquals("content://cover", preview.imageUrl)
        assertTrue(preview.date.isNotBlank())
    }

    @Test
    fun `empty draft does not invent mock tour content`() {
        val preview = GuideTourPublishUiState().toPreviewDetailUiState()

        assertEquals("", preview.title)
        assertEquals("", preview.description)
        assertEquals("", preview.meetingPoint)
        assertEquals(0L, preview.priceMinor)
        assertEquals(0, preview.capacity)
    }
}
