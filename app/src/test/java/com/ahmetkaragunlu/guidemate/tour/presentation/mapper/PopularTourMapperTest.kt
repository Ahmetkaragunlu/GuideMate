package com.ahmetkaragunlu.guidemate.tour.presentation.mapper

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchItem
import java.time.Instant
import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Test

class PopularTourMapperTest {
    @Test
    fun `popular card uses canonical tour session and guide values`() {
        val item =
            TourSearchItem(
                tourId = "tour-1",
                sessionId = "session-1",
                title = "Ayasofya Tarih Turu",
                category = TourCategory.CULTURE,
                cityName = "İstanbul",
                countryCode = "TR",
                cityPlaceId = "istanbul",
                startsAt = Instant.parse("2027-05-24T06:00:00Z"),
                timeZoneId = "Europe/Istanbul",
                durationMinutes = 180,
                priceMinor = 150_000,
                currencyCode = "USD",
                availableCapacity = 8,
                languageCodes = emptyList(),
                cover = MediaReference("media-1", "content://tour-cover"),
                averageRating = 4.96777,
                reviewCount = 120,
                guide =
                    GuidePublicSummary(
                        id = 1L,
                        displayName = "Ahmet Yılmaz",
                        profileImageUrl = "content://guide-avatar",
                    ),
            )

        val card = item.toPopularTourCardUiModel(Locale.US)

        assertEquals(item.sessionId, card.id)
        assertEquals(item.title, card.title)
        assertEquals(item.cover.imageUrl, card.imageUrl)
        assertEquals("5.0", card.rating)
        assertEquals(item.priceMinor, card.priceMinor)
        assertEquals("", card.languagesFlag)
        assertEquals("", card.languagesText)
        assertEquals(item.guide.displayName, card.guideName)
        assertEquals(R.drawable.ic_default_avatar, card.guideImageResId)
        assertEquals(item.guide.profileImageUrl, card.guideImageUrl)
    }
}
