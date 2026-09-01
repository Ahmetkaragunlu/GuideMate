package com.ahmetkaragunlu.guidemate.reservation.presentation.mapper

import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationActor
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRefundEligibility
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationSnapshot
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailStatus
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TouristReservationMapperTest {
    @Test
    fun `trip card and detail use the same reservation snapshot`() {
        val reservation = reservation()

        val trip = reservation.toTripUiModel()
        val detail = reservation.toTourDetailUiState()

        assertEquals(trip.title, detail.title)
        assertEquals(trip.date, detail.date)
        assertEquals(trip.location, detail.location)
        assertEquals(trip.imageUrl, detail.imageUrl)
        assertEquals(trip.category, detail.category)
        assertEquals(trip.languagesText, detail.languagesText)
        assertEquals(reservation.unitPriceMinor, detail.priceMinor)
        assertEquals(reservation.totalPriceMinor, trip.totalPriceMinor)
        assertEquals(reservation.participantCount, detail.reservedParticipantCount)
        assertEquals(reservation.snapshot.guide.id, detail.guideId)
    }

    @Test
    fun `cancelled reservation remains distinct from completed trip`() {
        val reservation =
            reservation().copy(
                status = TouristReservationStatus.CANCELLED,
                cancellationActor = ReservationCancellationActor.GUIDE,
                cancellationReason = "Olumsuz hava koşulları",
                refundEligibility = ReservationRefundEligibility.FULL_REFUND,
            )

        val trip = reservation.toTripUiModel()

        assertTrue(trip.isPast)
        assertEquals(TourDetailStatus.CANCELLED, trip.sessionStatus)
        assertEquals("Olumsuz hava koşulları", trip.cancellationReason)
    }

    @Test
    fun `completed reservation maps to past without changing snapshot`() {
        val reservation = reservation().copy(status = TouristReservationStatus.COMPLETED)

        val detail = reservation.toTourDetailUiState()

        assertEquals(TourDetailStatus.COMPLETED, detail.sessionStatus)
        assertEquals(reservation.snapshot.title, detail.title)
        assertEquals(reservation.snapshot.meetingPoint, detail.meetingPoint)
    }

    private fun reservation(): TouristReservation =
        TouristReservation(
            id = "reservation-1",
            tourSessionId = "session-1",
            version = 2,
            participantCount = 2,
            unitPriceMinor = 10_000,
            totalPriceMinor = 20_000,
            currencyCode = "USD",
            status = TouristReservationStatus.CONFIRMED,
            cancellationPolicyCode = "STANDARD_48_HOURS",
            cancellationPolicyVersion = 1,
            snapshot =
                TouristReservationSnapshot(
                    tourId = "tour-1",
                    guide =
                        GuidePublicSummary(
                            id = 10L,
                            displayName = "Ahmet Yılmaz",
                        ),
                    title = "Kapadokya Turu",
                    description = "Tur açıklaması",
                    countryCode = "TR",
                    country = "Türkiye",
                    cityPlaceId = "city-1",
                    city = "Nevşehir",
                    timeZoneId = "Europe/Istanbul",
                    category = TourCategory.CULTURE,
                    languages = listOf(TourLanguage("tr", "🇹🇷", "Türkçe", "TR")),
                    coverMediaId = null,
                    coverImageUrl = null,
                    startsAt = Instant.parse("2027-05-24T06:00:00Z"),
                    durationMinutes = 180,
                    meetingPoint = "Göreme merkez",
                    unitPriceMinor = 10_000,
                ),
        )
}
