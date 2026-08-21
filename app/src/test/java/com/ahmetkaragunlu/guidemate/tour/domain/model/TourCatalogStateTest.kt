package com.ahmetkaragunlu.guidemate.tour.domain.model

import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourCatalogState
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.effectiveStatus
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TourCatalogStateTest {
    @Test
    fun `expired open session moves from active to past`() {
        val session =
            session(
                startsAt = NOW.minusSeconds(7_200),
                durationMinutes = 60,
                status = TourSessionStatus.OPEN_FOR_BOOKING,
            )
        val catalog = catalog(session)

        assertTrue(catalog.activeTourItemsAt(NOW).isEmpty())
        assertTrue(catalog.bookableTourItemsAt(NOW).isEmpty())
        assertEquals(listOf(session.id), catalog.pastTourItemsAt(NOW).map { it.session.id })
        assertEquals(TourSessionStatus.COMPLETED, session.effectiveStatus(NOW))
    }

    @Test
    fun `ongoing open session stays active but is no longer published`() {
        val session =
            session(
                startsAt = NOW.minusSeconds(1_800),
                durationMinutes = 60,
                status = TourSessionStatus.OPEN_FOR_BOOKING,
            )
        val catalog = catalog(session)

        assertEquals(listOf(session.id), catalog.activeTourItemsAt(NOW).map { it.session.id })
        assertTrue(catalog.bookableTourItemsAt(NOW).isEmpty())
        assertTrue(catalog.pastTourItemsAt(NOW).isEmpty())
    }

    @Test
    fun `future closed session stays active without being published`() {
        val session =
            session(
                startsAt = NOW.plusSeconds(3_600),
                durationMinutes = 60,
                status = TourSessionStatus.CLOSED,
            )
        val catalog = catalog(session)

        assertEquals(listOf(session.id), catalog.activeTourItemsAt(NOW).map { it.session.id })
        assertTrue(catalog.bookableTourItemsAt(NOW).isEmpty())
        assertTrue(catalog.pastTourItemsAt(NOW).isEmpty())
    }

    @Test
    fun `future approved open session is active and published`() {
        val session =
            session(
                startsAt = NOW.plusSeconds(3_600),
                durationMinutes = 60,
                status = TourSessionStatus.OPEN_FOR_BOOKING,
            )
        val catalog = catalog(session)

        assertEquals(listOf(session.id), catalog.activeTourItemsAt(NOW).map { it.session.id })
        assertEquals(listOf(session.id), catalog.bookableTourItemsAt(NOW).map { it.session.id })
        assertTrue(catalog.pastTourItemsAt(NOW).isEmpty())
    }

    @Test
    fun `published guide items contain only the requested guide tours`() {
        val anotherGuideTour =
            tour.copy(
                id = "tour-2",
                guide = tour.guide.copy(id = "guide-2"),
            )
        val currentGuideSession =
            session(
                startsAt = NOW.plusSeconds(3_600),
                durationMinutes = 60,
                status = TourSessionStatus.OPEN_FOR_BOOKING,
            )
        val anotherGuideSession =
            session(
                id = "session-2",
                tourId = anotherGuideTour.id,
                startsAt = NOW.plusSeconds(7_200),
                durationMinutes = 60,
                status = TourSessionStatus.OPEN_FOR_BOOKING,
            )
        val catalog =
            TourCatalogState(
                tours = listOf(tour, anotherGuideTour),
                sessions = listOf(currentGuideSession, anotherGuideSession),
            )

        val result =
            catalog.bookableTourItemsForGuideAt(
                guideId = tour.guide.id,
                now = NOW,
            )

        assertEquals(listOf(tour.id), result.map { it.tour.id })
    }

    private fun catalog(session: TourSession): TourCatalogState =
        TourCatalogState(
            tours = listOf(tour),
            sessions = listOf(session),
        )

    private fun session(
        id: String = "session-1",
        tourId: String = tour.id,
        startsAt: Instant,
        durationMinutes: Int,
        status: TourSessionStatus,
    ): TourSession =
        TourSession(
            id = id,
            tourId = tourId,
            meetingPoint = "Meeting point",
            startsAt = startsAt,
            durationMinutes = durationMinutes,
            priceMinor = 10_000,
            capacity = 10,
            bookedCount = 2,
            status = status,
        )

    private companion object {
        val NOW: Instant = Instant.parse("2026-07-26T12:00:00Z")
        val tour =
            Tour(
                id = "tour-1",
                guide =
                    GuidePublicSummary(
                        id = "guide-1",
                        displayName = "Test Guide",
                        profileImageResId = 0,
                    ),
                title = "Test Tour",
                description = "Test description",
                country = "Türkiye",
                city = "İstanbul",
                timeZoneId = "Europe/Istanbul",
                category = TourCategory.CULTURE,
                languages = emptyList(),
                coverImageResId = 0,
                approvalStatus = TourApprovalStatus.APPROVED,
            )
    }
}
