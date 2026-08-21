package com.ahmetkaragunlu.guidemate.reservation.presentation.mapper

import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import com.ahmetkaragunlu.guidemate.tour.data.mock.TourCatalogStore
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.reservation.data.mock.TouristReservationStore
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TouristReservationMapperTest {
    private lateinit var tourCatalogStore: TourCatalogStore
    private lateinit var reservationStore: TouristReservationStore

    @Before
    fun setUp() {
        tourCatalogStore = TourCatalogStore()
        reservationStore = TouristReservationStore(tourCatalogStore)
    }

    @Test
    fun `trip card and detail use the same reservation snapshot`() {
        val reservation = reservationStore.reservations.value.first()
        val currentTour = currentTour(reservation.tourSessionId)

        val trip = reservation.toTripUiModel(currentTour, REVIEW_NOW)
        val detail = reservation.toTourDetailUiState(currentTour, REVIEW_NOW)

        assertEquals(trip.title, detail.title)
        assertEquals(trip.date, detail.date)
        assertEquals(trip.location, detail.location)
        assertEquals(trip.imageResId, detail.imageResId)
        assertEquals(trip.imageUrl, detail.imageUrl)
        assertEquals(trip.category, detail.category)
        assertEquals(trip.languagesFlag, detail.languagesFlag)
        assertEquals(trip.languagesText, detail.languagesText)
        assertEquals(trip.priceMinor, detail.priceMinor)
        assertEquals(trip.rating, detail.rating)
        assertEquals(trip.reviewCount, detail.reviewCount)
    }

    @Test
    fun `reservation snapshot is unchanged by later tour edits`() {
        val reservation = reservationStore.reservations.value.first()
        val currentTour = currentTour(reservation.tourSessionId)
        val editedTour =
            currentTour.copy(
                tour = currentTour.tour.copy(title = "Güncellenmiş Başlık"),
                session = currentTour.session.copy(priceMinor = 999_999),
            )

        val detail = reservation.toTourDetailUiState(editedTour, REVIEW_NOW)

        assertEquals(reservation.snapshot.title, detail.title)
        assertEquals(reservation.snapshot.unitPriceMinor, detail.priceMinor)
    }

    @Test
    fun `reservation detail remains available when current tour is unavailable`() {
        val reservation = reservationStore.reservations.value.first()

        val detail = reservation.toTourDetailUiState(currentTour = null, now = REVIEW_NOW)

        assertEquals(reservation.snapshot.tourId, detail.tourId)
        assertEquals(reservation.snapshot.title, detail.title)
        assertEquals(reservation.snapshot.description, detail.description)
        assertEquals(reservation.snapshot.meetingPoint, detail.meetingPoint)
        assertEquals(reservation.snapshot.unitPriceMinor, detail.priceMinor)
    }

    @Test
    fun `guide cancellation moves reservation to past and exposes cancellation`() {
        val reservation = reservationStore.reservations.value.first()
        val currentTour = currentTour(reservation.tourSessionId)
        val cancelledTour =
            currentTour.copy(
                session =
                    currentTour.session.copy(
                        status = TourSessionStatus.CANCELLED,
                        cancellationReason = "Olumsuz hava koşulları",
                    ),
            )

        val trip = reservation.toTripUiModel(cancelledTour, REVIEW_NOW)

        assertTrue(trip.isPast)
        assertEquals(TourDetailStatus.CANCELLED, trip.sessionStatus)
        assertEquals("Olumsuz hava koşulları", trip.cancellationReason)
    }

    @Test
    fun `tourist cancellation changes only reservation state and moves trip to past`() {
        val reservation = reservationStore.reservations.value.first()
        val currentTour = currentTour(reservation.tourSessionId)
        val originalSessionStatus = currentTour.session.status

        assertTrue(reservationStore.cancelReservation(reservation.id))
        assertFalse(reservationStore.cancelReservation(reservation.id))

        val cancelledReservation =
            checkNotNull(
                reservationStore.reservations.value.firstOrNull {
                    it.id == reservation.id
                },
            )
        val trip = cancelledReservation.toTripUiModel(currentTour, REVIEW_NOW)

        assertEquals(TouristReservationStatus.CANCELLED, cancelledReservation.status)
        assertEquals(TourDetailStatus.CANCELLED, trip.sessionStatus)
        assertTrue(trip.isPast)
        assertEquals(originalSessionStatus, currentTour.session.status)
    }

    private fun currentTour(sessionId: String): TourWithSession =
        checkNotNull(tourCatalogStore.state.value.findBySessionId(sessionId))

    private companion object {
        val REVIEW_NOW: Instant = Instant.parse("2026-07-26T12:00:00Z")
    }
}
