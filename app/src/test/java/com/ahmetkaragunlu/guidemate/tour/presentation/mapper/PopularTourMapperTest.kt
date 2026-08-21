package com.ahmetkaragunlu.guidemate.tour.presentation.mapper

import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.Tour
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Test

class PopularTourMapperTest {
    @Test
    fun `popular card uses canonical tour session and guide values`() {
        val item =
            TourWithSession(
                tour =
                    Tour(
                        id = "tour-1",
                        guide =
                            GuidePublicSummary(
                                id = "guide-1",
                                displayName = "Ahmet Yılmaz",
                                profileImageResId = 11,
                                profileImageUrl = "content://guide-avatar",
                            ),
                        title = "Ayasofya Tarih Turu",
                        description = "Tur açıklaması",
                        country = "Türkiye",
                        city = "İstanbul",
                        timeZoneId = "Europe/Istanbul",
                        category = TourCategory.CULTURE,
                        languages =
                            listOf(
                                TourLanguage(
                                    code = "tr",
                                    flagEmoji = "🇹🇷",
                                    displayName = "Türkçe",
                                    shortCode = "TR",
                                ),
                            ),
                        coverImageResId = 22,
                        coverImageUrl = "content://tour-cover",
                        approvalStatus = TourApprovalStatus.APPROVED,
                        averageRating = 4.9,
                        reviewCount = 120,
                    ),
                session =
                    TourSession(
                        id = "session-1",
                        tourId = "tour-1",
                        meetingPoint = "Ayasofya Meydanı",
                        startsAt = Instant.parse("2027-05-24T06:00:00Z"),
                        durationMinutes = 180,
                        priceMinor = 150_000,
                        capacity = 12,
                        bookedCount = 4,
                        status = TourSessionStatus.OPEN_FOR_BOOKING,
                    ),
            )

        val card = item.toPopularTourCardUiModel()

        assertEquals(item.session.id, card.id)
        assertEquals(item.tour.title, card.title)
        assertEquals(item.tour.coverImageResId, card.imageResId)
        assertEquals(item.tour.coverImageUrl, card.imageUrl)
        assertEquals(item.session.priceMinor, card.priceMinor)
        assertEquals("🇹🇷", card.languagesFlag)
        assertEquals("TR", card.languagesText)
        assertEquals(item.tour.guide.displayName, card.guideName)
        assertEquals(item.tour.guide.profileImageResId, card.guideImageResId)
        assertEquals(item.tour.guide.profileImageUrl, card.guideImageUrl)
    }
}
