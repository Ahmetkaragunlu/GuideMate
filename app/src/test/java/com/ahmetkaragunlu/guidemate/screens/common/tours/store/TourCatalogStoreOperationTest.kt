package com.ahmetkaragunlu.guidemate.screens.common.tours.store

import com.ahmetkaragunlu.guidemate.screens.common.tours.model.operation.TourOperationResult
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.session.TourSessionStatus
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TourCatalogStoreOperationTest {
    @Test
    fun `booking switch returns actionable failure reasons`() {
        val store = TourCatalogStore()
        val now = Instant.parse("2026-07-26T12:00:00Z")

        assertEquals(
            TourOperationResult.SESSION_NOT_FOUND,
            store.setSessionBookingOpen(
                sessionId = "missing-session",
                isOpen = true,
                now = now,
            ),
        )
        assertEquals(
            TourOperationResult.SESSION_ALREADY_STARTED,
            store.setSessionBookingOpen(
                sessionId = "session-bogaz-past",
                isOpen = true,
                now = now,
            ),
        )
        assertEquals(
            TourOperationResult.TOUR_NOT_APPROVED,
            store.setSessionBookingOpen(
                sessionId = "session-sultanahmet-pending",
                isOpen = true,
                now = now,
            ),
        )
    }

    @Test
    fun `booking switch updates session when transition is allowed`() {
        val store = TourCatalogStore()
        val now = Instant.parse("2026-07-26T12:00:00Z")
        val sessionId = "session-kapadokya-active"

        assertEquals(
            TourOperationResult.SUCCESS,
            store.setSessionBookingOpen(
                sessionId = sessionId,
                isOpen = false,
                now = now,
            ),
        )
        assertEquals(
            TourSessionStatus.CLOSED,
            store.state.value.findBySessionId(sessionId)?.session?.status,
        )
    }

    @Test
    fun `archive returns result and removes rejected tour from review`() {
        val store = TourCatalogStore()

        assertEquals(
            TourOperationResult.TOUR_NOT_ARCHIVABLE,
            store.archiveRejectedTour("tour-kapadokya"),
        )
        assertEquals(
            TourOperationResult.SUCCESS,
            store.archiveRejectedTour("tour-mardin-rejected"),
        )
        assertNull(
            store.state.value.reviewTourItems.firstOrNull {
                it.tour.id == "tour-mardin-rejected"
            },
        )
    }
}
