package com.ahmetkaragunlu.guidemate.tour.domain.model

import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourBookingAvailability
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.resolveBookingAvailability
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TourBookingAvailabilityTest {
    private lateinit var availableTour: TourWithSession

    @Before
    fun setUp() {
        availableTour =
            TourWithSession(
                tour =
                    Tour(
                        id = "tour-1",
                        guide = GuidePublicSummary("guide-1", "Test Guide", 0),
                        title = "Test Tour",
                        description = "Test description",
                        country = "Türkiye",
                        city = "İstanbul",
                        timeZoneId = "Europe/Istanbul",
                        category = TourCategory.CULTURE,
                        languages = emptyList(),
                        coverImageResId = 0,
                        approvalStatus = TourApprovalStatus.APPROVED,
                    ),
                session =
                    TourSession(
                        id = "session-1",
                        tourId = "tour-1",
                        meetingPoint = "Meeting point",
                        startsAt = NOW.plusSeconds(3_600),
                        durationMinutes = 120,
                        priceMinor = 10_000,
                        capacity = 10,
                        bookedCount = 2,
                        status = TourSessionStatus.OPEN_FOR_BOOKING,
                    ),
            )
    }

    @Test
    fun `approved future open session with capacity is available`() {
        assertEquals(
            TourBookingAvailability.AVAILABLE,
            availableTour.resolveBookingAvailability(hasReservation = false, now = NOW),
        )
    }

    @Test
    fun `existing reservation prevents another booking`() {
        assertEquals(
            TourBookingAvailability.ALREADY_RESERVED,
            availableTour.resolveBookingAvailability(hasReservation = true, now = NOW),
        )
    }

    @Test
    fun `unapproved and closed sessions are unavailable`() {
        assertEquals(
            TourBookingAvailability.NOT_APPROVED,
            availableTour
                .copy(tour = availableTour.tour.copy(approvalStatus = TourApprovalStatus.PENDING_REVIEW))
                .resolveBookingAvailability(hasReservation = false, now = NOW),
        )
        assertEquals(
            TourBookingAvailability.CLOSED,
            availableTour
                .copy(session = availableTour.session.copy(status = TourSessionStatus.CLOSED))
                .resolveBookingAvailability(hasReservation = false, now = NOW),
        )
    }

    @Test
    fun `full and started sessions are unavailable`() {
        assertEquals(
            TourBookingAvailability.FULL,
            availableTour
                .copy(session = availableTour.session.copy(bookedCount = availableTour.session.capacity))
                .resolveBookingAvailability(hasReservation = false, now = NOW),
        )
        assertEquals(
            TourBookingAvailability.STARTED,
            availableTour
                .copy(session = availableTour.session.copy(startsAt = NOW.minusSeconds(60)))
                .resolveBookingAvailability(hasReservation = false, now = NOW),
        )
    }

    @Test
    fun `cancelled and completed sessions keep their terminal reason`() {
        assertEquals(
            TourBookingAvailability.CANCELLED,
            availableTour
                .copy(session = availableTour.session.copy(status = TourSessionStatus.CANCELLED))
                .resolveBookingAvailability(hasReservation = false, now = NOW),
        )
        assertEquals(
            TourBookingAvailability.COMPLETED,
            availableTour
                .copy(session = availableTour.session.copy(status = TourSessionStatus.COMPLETED))
                .resolveBookingAvailability(hasReservation = false, now = NOW),
        )
    }

    @Test
    fun `expired open session is treated as completed`() {
        val expiredSession =
            availableTour.session.copy(
                startsAt = NOW.minusSeconds(7_200),
                durationMinutes = 60,
            )

        assertEquals(
            TourBookingAvailability.COMPLETED,
            availableTour
                .copy(session = expiredSession)
                .resolveBookingAvailability(hasReservation = false, now = NOW),
        )
    }

    @Test
    fun `missing session is unavailable unless reservation snapshot exists`() {
        assertEquals(
            TourBookingAvailability.UNAVAILABLE,
            null.resolveBookingAvailability(hasReservation = false, now = NOW),
        )
        assertEquals(
            TourBookingAvailability.ALREADY_RESERVED,
            null.resolveBookingAvailability(hasReservation = true, now = NOW),
        )
    }

    private companion object {
        val NOW: Instant = Instant.parse("2026-07-26T12:00:00Z")
    }
}
