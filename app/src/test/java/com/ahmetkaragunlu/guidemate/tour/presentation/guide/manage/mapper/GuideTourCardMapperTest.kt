package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.mapper

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.testing.tour.testGuideTourCard
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GuideTourCardMapperTest {
    @Test
    fun `maps guide tour card fields into cohesive ui groups`() {
        val domain = testGuideTourCard(tourId = "tour-1", sessionId = "session-1")

        val result = domain.toGuideTourCardUiModel()

        assertEquals("session-1", result.id)
        assertEquals("tour-1", result.tourId)
        assertEquals(R.drawable.ic_image_unavailable, result.media.imageResId)
        assertEquals(domain.cover?.imageUrl, result.media.imageUrl)
        assertEquals(domain.stats.bookedCount, result.participantCount)
        assertEquals(domain.pricing.priceMinor, result.pricing.priceMinor)
        assertEquals(domain.pricing.netEarningsMinor, result.pricing.earningsMinor)
        assertEquals(domain.stats.averageRating, result.rating?.value)
        assertEquals(domain.stats.reviewCount, result.rating?.reviewCount)
        assertEquals(domain.approvalStatus, result.status.approvalStatus)
        assertEquals(domain.sessionStatus, result.status.sessionStatus)
        assertTrue(result.status.isBookingOpen)
    }

    @Test
    fun `omits rating group when tour has no reviews`() {
        val domain =
            testGuideTourCard(tourId = "tour-1", sessionId = "session-1").let { card ->
                card.copy(
                    stats =
                        card.stats.copy(
                            averageRating = 0.0,
                            reviewCount = 0,
                        ),
                )
            }

        val result = domain.toGuideTourCardUiModel()

        assertNull(result.rating)
        assertFalse(result.status.canArchive)
    }
}
