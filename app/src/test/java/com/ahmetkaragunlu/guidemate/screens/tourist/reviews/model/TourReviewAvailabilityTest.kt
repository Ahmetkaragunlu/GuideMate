package com.ahmetkaragunlu.guidemate.screens.tourist.reviews.model

import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailStatus
import com.ahmetkaragunlu.guidemate.screens.common.tours.store.TourCatalogStore
import com.ahmetkaragunlu.guidemate.screens.tourist.reservations.model.CreateTourReviewRequest
import com.ahmetkaragunlu.guidemate.screens.tourist.reservations.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.screens.tourist.reservations.store.TouristReservationStore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TourReviewAvailabilityTest {
    private lateinit var reservationStore: TouristReservationStore

    @Before
    fun setUp() {
        reservationStore = TouristReservationStore(TourCatalogStore())
    }

    @Test
    fun `completed reservation can be reviewed`() {
        val reservation = reservationStore.reservations.value.first()

        val availability =
            resolveTourReviewAvailability(
                reservation = reservation,
                detailStatus = TourDetailStatus.COMPLETED,
            )

        assertEquals(TourReviewAvailability.AVAILABLE, availability)
    }

    @Test
    fun `cancelled reservation cannot be reviewed`() {
        val reservation =
            reservationStore.reservations.value
                .first()
                .copy(status = TouristReservationStatus.CANCELLED)

        val availability =
            resolveTourReviewAvailability(
                reservation = reservation,
                detailStatus = TourDetailStatus.COMPLETED,
            )

        assertEquals(TourReviewAvailability.UNAVAILABLE, availability)
    }

    @Test
    fun `tour without reservation cannot be reviewed`() {
        val availability =
            resolveTourReviewAvailability(
                reservation = null,
                detailStatus = TourDetailStatus.COMPLETED,
            )

        assertEquals(TourReviewAvailability.UNAVAILABLE, availability)
    }

    @Test
    fun `reservation accepts only one review`() {
        val reservation = reservationStore.reservations.value.first()
        val request =
            CreateTourReviewRequest(
                reservationId = reservation.id,
                rating = 5,
                comment = "Harika bir deneyimdi.",
            )

        assertTrue(reservationStore.submitReview(request))
        assertFalse(reservationStore.submitReview(request))

        val reviewedReservation =
            checkNotNull(
                reservationStore.reservations.value.firstOrNull {
                    it.id == reservation.id
                },
            )
        assertEquals(
            TourReviewAvailability.SUBMITTED,
            resolveTourReviewAvailability(
                reservation = reviewedReservation,
                detailStatus = TourDetailStatus.COMPLETED,
            ),
        )
    }
}
