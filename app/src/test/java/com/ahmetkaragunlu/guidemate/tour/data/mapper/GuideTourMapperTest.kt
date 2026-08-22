package com.ahmetkaragunlu.guidemate.tour.data.mapper

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.GuideDashboardResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.GuideTourCardResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.PublicGuideSummaryResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourDetailResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourSessionResponseDto
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class GuideTourMapperTest {
    @Test
    fun `maps guide card without losing backend versions capacity or earnings`() {
        val page =
            ApiPageResponse(
                content = listOf(cardResponse()),
                page = 1,
                size = 20,
                totalElements = 21,
                totalPages = 2,
                isFirst = false,
                isLast = true,
            )

        val result = page.toDomain()
        val card = result.items.single()

        assertEquals(1, result.page)
        assertEquals(21L, result.totalElements)
        assertEquals("tour-1", card.tourId)
        assertEquals(7L, card.tourVersion)
        assertEquals(3L, card.sessionVersion)
        assertEquals(12, card.capacity)
        assertEquals(4, card.bookedCount)
        assertEquals(98_00L, card.netEarningsMinor)
        assertEquals(TourSessionStatus.OPEN_FOR_BOOKING, card.sessionStatus)
    }

    @Test
    fun `maps owned detail and selected session canonical data`() {
        val result = detailResponse().toDomain()

        assertEquals("tour-1", result.tour.id)
        assertEquals(7L, result.tour.version)
        assertEquals("media-cover", result.tour.coverMediaId)
        assertEquals(TourApprovalStatus.APPROVED, result.tour.approvalStatus)
        assertEquals(46L, result.tour.reviewCount)
        assertEquals(12, result.session("session-1")?.capacity)
        assertEquals("USD", result.session("session-1")?.currencyCode)
    }

    @Test
    fun `maps dashboard projection as a single source of guide counters`() {
        val result =
            GuideDashboardResponseDto(
                activeSessionCount = 2,
                pendingReviewCount = 1,
                completedSessionCount = 12,
                totalParticipantCount = 84,
                averageRating = 4.8,
                reviewCount = 46,
                level = "SUPER",
                currentMonthEarningsMinor = 12_500,
                currencyCode = "USD",
            ).toDomain()

        assertEquals(2L, result.activeSessionCount)
        assertEquals(84L, result.totalParticipantCount)
        assertEquals(GuideLevelTier.SUPER, result.level)
        assertEquals(12_500L, result.currentMonthEarningsMinor)
    }

    private fun cardResponse(): GuideTourCardResponseDto =
        GuideTourCardResponseDto(
            tourId = "tour-1",
            sessionId = "session-1",
            tourVersion = 7,
            sessionVersion = 3,
            title = "Tarihi İstanbul",
            cityName = "İstanbul",
            countryCode = "TR",
            timeZoneId = "Europe/Istanbul",
            categoryCode = "culture",
            languageCodes = listOf("tr", "en"),
            cover = MediaReferenceResponseDto("media-cover", "https://example.com/cover"),
            startsAt = "2027-05-24T06:00:00Z",
            durationMinutes = 180,
            priceMinor = 15_000,
            currencyCode = "USD",
            bookedCount = 4,
            capacity = 12,
            averageRating = 4.8,
            reviewCount = 46,
            netEarningsMinor = 98_00,
            approvalStatus = "APPROVED",
            sessionStatus = "OPEN_FOR_BOOKING",
            rejectionReason = null,
            canArchive = false,
        )

    private fun detailResponse(): TourDetailResponseDto =
        TourDetailResponseDto(
            tourId = "tour-1",
            version = 7,
            guide =
                PublicGuideSummaryResponseDto(
                    guideId = 42,
                    displayName = "Ahmet Karagünlü",
                    avatar = null,
                ),
            title = "Tarihi İstanbul",
            description = "İstanbul'un tarihi rotalarını birlikte keşfedin.",
            countryCode = "TR",
            cityPlaceId = "place-istanbul",
            cityName = "İstanbul",
            timeZoneId = "Europe/Istanbul",
            categoryCode = "culture",
            languageCodes = emptyList(),
            cover = MediaReferenceResponseDto("media-cover", "https://example.com/cover"),
            approvalStatus = "APPROVED",
            submittedAt = "2026-08-01T10:00:00Z",
            publishedAt = "2026-08-02T10:00:00Z",
            reviewedAt = "2026-08-02T09:30:00Z",
            rejectionReason = null,
            averageRating = 4.8,
            reviewCount = 46,
            sessions = listOf(sessionResponse()),
        )

    private fun sessionResponse(): TourSessionResponseDto =
        TourSessionResponseDto(
            sessionId = "session-1",
            tourId = "tour-1",
            version = 3,
            meetingPoint = "Sultanahmet Meydanı",
            startsAt = "2027-05-24T06:00:00Z",
            durationMinutes = 180,
            priceMinor = 15_000,
            currencyCode = "USD",
            capacity = 12,
            bookedCount = 4,
            availableCapacity = 8,
            status = "OPEN_FOR_BOOKING",
            cancellationActor = null,
            cancellationReason = null,
            cancelledAt = null,
        )
}
