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
        val item = tourSearchItem()

        val card = item.toPopularTourCardUiModel(Locale.US)

        assertEquals(item.sessionId, card.id)
        assertEquals(item.title, card.title)
        assertEquals(item.cover.imageUrl, card.media.imageUrl)
        assertEquals("5.0", card.rating.value)
        assertEquals("(120)", card.rating.reviewCount)
        assertEquals(item.priceMinor, card.priceMinor)
        assertEquals("", card.languages.flags)
        assertEquals("", card.languages.shortCodes)
        assertEquals(item.guide.displayName, card.guide.name)
        assertEquals(R.drawable.ic_default_avatar, card.guide.imageResId)
        assertEquals(item.guide.profileImageUrl, card.guide.imageUrl)
    }

    @Test
    fun `search result preserves rating capacity media and guide values`() {
        val item = tourSearchItem()

        val card = item.toSearchResultUiModel(Locale.US)

        assertEquals(item.sessionId, card.sessionId)
        assertEquals(item.cover.imageUrl, card.media.imageUrl)
        assertEquals(item.averageRating, card.rating?.value)
        assertEquals(item.reviewCount, card.rating?.reviewCount)
        assertEquals(item.availableCapacity, card.availableCapacity)
        assertEquals(item.guide.displayName, card.guide.name)
        assertEquals(item.guide.profileImageUrl, card.guide.imageUrl)
    }

    private fun tourSearchItem(): TourSearchItem =
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
}
