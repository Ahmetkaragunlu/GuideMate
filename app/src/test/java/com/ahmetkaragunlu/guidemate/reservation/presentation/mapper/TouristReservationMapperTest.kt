package com.ahmetkaragunlu.guidemate.reservation.presentation.mapper

import com.ahmetkaragunlu.guidemate.R
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
import org.junit.Test

class TouristReservationMapperTest {
    @Test
    fun `trip card and detail use the same reservation snapshot`() {
        val reservation = reservation()

        val trip = reservation.toTripUiModel()
        val detail = reservation.toTourDetailUiState()

        assertEquals(trip.title, detail.tour.title)
        assertEquals(trip.date, detail.session.date)
        assertEquals(trip.location, detail.tour.location)
        assertEquals(trip.imageUrl, detail.tour.imageUrl)
        assertEquals(trip.category, detail.tour.category)
        assertEquals(trip.languagesText, detail.tour.languagesText)
        assertEquals(reservation.unitPriceMinor, detail.session.priceMinor)
        assertEquals(reservation.totalPriceMinor, trip.totalPriceMinor)
        assertEquals(null, detail.session.reservedParticipantCount)
        assertEquals(reservation.averageRating, detail.tour.rating ?: 0.0, 0.0)
        assertEquals(reservation.reviewCount, detail.tour.reviewCount)
        assertEquals(reservation.bookedCount, detail.session.bookedCount)
        assertEquals(reservation.capacity, detail.session.capacity)
        assertEquals(reservation.snapshot.guide.id, detail.guide.id)
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

        assertEquals(TourDetailStatus.CANCELLED, trip.sessionStatus)
        assertEquals(R.string.tour_cancelled_status, trip.cancellationTitleResId)
        assertEquals("Olumsuz hava koşulları", trip.cancellationReason)
    }

    @Test
    fun `tourist cancellation is labelled as own reservation cancellation`() {
        val trip =
            reservation()
                .copy(
                    status = TouristReservationStatus.CANCELLED,
                    cancellationActor = ReservationCancellationActor.TOURIST,
                ).toTripUiModel()

        assertEquals(R.string.reservation_cancelled_success, trip.cancellationTitleResId)
    }

    @Test
    fun `completed reservation maps to past without changing snapshot`() {
        val reservation = reservation().copy(status = TouristReservationStatus.COMPLETED)

        val detail = reservation.toTourDetailUiState()

        assertEquals(TourDetailStatus.COMPLETED, detail.session.status)
        assertEquals(reservation.snapshot.title, detail.tour.title)
        assertEquals(reservation.snapshot.meetingPoint, detail.session.meetingPoint)
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
            cancellationPolicyCode = "FULL_REFUND_48_HOURS",
            cancellationPolicyVersion = 1,
            averageRating = 4.7,
            reviewCount = 12,
            bookedCount = 5,
            capacity = 8,
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
