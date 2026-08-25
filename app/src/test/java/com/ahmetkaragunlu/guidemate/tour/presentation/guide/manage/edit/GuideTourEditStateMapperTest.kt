package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit

import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditContentFormState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditIdentityState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditSessionFormState
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GuideTourEditStateMapperTest {
    @Test
    fun `content and session changes are detected independently`() {
        val original = validState()

        assertFalse(original.hasChangesFrom(original))
        val contentChanged = original.copy(content = original.content.copy(title = "Yeni Başlık"))
        val sessionChanged = original.copy(session = original.session.copy(capacity = "12"))
        assertTrue(contentChanged.hasContentChangesFrom(original))
        assertFalse(contentChanged.hasSessionChangesFrom(original))
        assertTrue(sessionChanged.hasSessionChangesFrom(original))
        assertFalse(sessionChanged.hasContentChangesFrom(original))
    }

    @Test
    fun `valid state maps to backend content and session inputs`() {
        val state = validState()

        val content = state.toContentInputOrNull(coverMediaId = "media-1")
        val session = state.toSessionInputOrNull()

        assertNotNull(content)
        assertEquals("Test Turu", content?.title)
        assertEquals("media-1", content?.coverMediaId)
        assertNotNull(session)
        assertEquals(12_500L, session?.priceMinor)
        assertEquals(10, session?.capacity)
        assertEquals(Instant.parse("2027-05-24T09:30:00Z"), session?.startsAt)
    }

    @Test
    fun `incomplete state does not create backend inputs`() {
        val state = validState()
        assertNull(
            state
                .copy(content = state.content.copy(category = null))
                .toContentInputOrNull(coverMediaId = "media-1"),
        )
        assertNull(
            state.copy(session = state.session.copy(meetingPoint = "")).toSessionInputOrNull(),
        )
    }

    private fun validState() =
        GuideTourEditUiState(
            identity =
                GuideTourEditIdentityState(
                    countryCode = "TR",
                    location = "İstanbul",
                    cityPlaceId = "istanbul-place-id",
                    timeZoneId = "UTC",
                ),
            content =
                GuideTourEditContentFormState(
                    title = "Test Turu",
                    description = "Test açıklaması",
                    category = TourCategory.CULTURE,
                    languages =
                        listOf(
                            TourLanguage(
                                code = "tr",
                                flagEmoji = "",
                                displayName = "Türkçe",
                                shortCode = "TR",
                            ),
                        ),
                ),
            session =
                GuideTourEditSessionFormState(
                    meetingPoint = "Sultanahmet Meydanı",
                    tourDate = LocalDate.of(2027, 5, 24),
                    startTime = LocalTime.of(9, 30),
                    durationMinutes = "120",
                    price = "125",
                    capacity = "10",
                ),
        )
}
