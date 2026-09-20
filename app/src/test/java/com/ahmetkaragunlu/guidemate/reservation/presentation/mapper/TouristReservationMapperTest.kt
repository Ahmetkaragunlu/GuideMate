package com.ahmetkaragunlu.guidemate.reservation.presentation.mapper

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationActor
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationDetails
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationPolicy
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationLocationSnapshot
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationPurchaseDetails
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRatingSummary
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationAttendance
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRefundEligibility
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationScheduleSnapshot
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
        assertEquals(reservation.purchase.unitPriceMinor, detail.session.priceMinor)
        assertEquals(reservation.purchase.totalPriceMinor, trip.totalPriceMinor)
        assertEquals(null, detail.session.reservedParticipantCount)
        assertEquals(reservation.rating.averageRating, detail.tour.rating ?: 0.0, 0.0)
        assertEquals(reservation.rating.reviewCount, detail.tour.reviewCount)
        assertEquals(reservation.attendance.bookedCount, detail.session.bookedCount)
        assertEquals(reservation.attendance.capacity, detail.session.capacity)
        assertEquals(reservation.snapshot.guide.id, detail.guide.id)
    }

    @Test
    fun `cancelled reservation remains distinct from completed trip`() {
        val reservation =
            reservation().copy(
                status = TouristReservationStatus.CANCELLED,
                cancellation =
                    reservation().cancellation.copy(
                        actor = ReservationCancellationActor.GUIDE,
                        reason = "Olumsuz hava koşulları",
                        refundEligibility = ReservationRefundEligibility.FULL_REFUND,
                    ),
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
                    cancellation = reservation().cancellation.copy(actor = ReservationCancellationActor.TOURIST),
                ).toTripUiModel()

        assertEquals(R.string.reservation_cancelled_success, trip.cancellationTitleResId)
    }

    @Test
    fun `completed reservation maps to past without changing snapshot`() {
        val reservation = reservation().copy(status = TouristReservationStatus.COMPLETED)

        val detail = reservation.toTourDetailUiState()

        assertEquals(TourDetailStatus.COMPLETED, detail.session.status)
        assertEquals(reservation.snapshot.title, detail.tour.title)
        assertEquals(reservation.snapshot.schedule.meetingPoint, detail.session.meetingPoint)
    }

    private fun reservation(): TouristReservation =
        TouristReservation(
            id = "reservation-1",
            tourSessionId = "session-1",
            version = 2,
            purchase =
                ReservationPurchaseDetails(
                    participantCount = 2,
                    unitPriceMinor = 10_000,
                    totalPriceMinor = 20_000,
                    currencyCode = "USD",
                ),
            status = TouristReservationStatus.CONFIRMED,
            cancellation =
                ReservationCancellationDetails(
                    actor = null,
                    reason = null,
                    cancelledAt = null,
                    refundEligibility = ReservationRefundEligibility.NOT_APPLICABLE,
                    policy = ReservationCancellationPolicy("FULL_REFUND_48_HOURS", 1),
                ),
            rating = ReservationRatingSummary(averageRating = 4.7, reviewCount = 12),
            attendance = ReservationAttendance(bookedCount = 5, capacity = 8),
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
                    location =
                        ReservationLocationSnapshot(
                            countryCode = "TR",
                            country = "Türkiye",
                            cityPlaceId = "city-1",
                            city = "Nevşehir",
                            timeZoneId = "Europe/Istanbul",
                        ),
                    category = TourCategory.CULTURE,
                    languages = listOf(TourLanguage("tr", "🇹🇷", "Türkçe", "TR")),
                    cover = null,
                    schedule =
                        ReservationScheduleSnapshot(
                            startsAt = Instant.parse("2027-05-24T06:00:00Z"),
                            durationMinutes = 180,
                            meetingPoint = "Göreme merkez",
                        ),
                ),
        )
}
