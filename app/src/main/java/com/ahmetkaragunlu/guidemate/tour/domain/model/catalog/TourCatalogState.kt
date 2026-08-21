package com.ahmetkaragunlu.guidemate.tour.domain.model.catalog

import com.ahmetkaragunlu.guidemate.tour.domain.model.Tour
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.isEffectivelyTerminal
import java.time.Instant

data class TourCatalogState(
    val tours: List<Tour> = emptyList(),
    val sessions: List<TourSession> = emptyList(),
) {
    val activeTourItems: List<TourWithSession>
        get() = activeTourItemsAt(Instant.now())

    val bookableTourItems: List<TourWithSession>
        get() = bookableTourItemsAt(Instant.now())

    val reviewTourItems: List<TourWithSession>
        get() =
            tours
                .filter { tour ->
                    tour.approvalStatus == TourApprovalStatus.PENDING_REVIEW ||
                        tour.approvalStatus == TourApprovalStatus.REJECTED
                }.mapNotNull { tour ->
                    sessions
                        .filter { it.tourId == tour.id }
                        .maxByOrNull { it.startsAt }
                        ?.let { session -> TourWithSession(tour = tour, session = session) }
                }.sortedByDescending { tourWithSession ->
                    tourWithSession.tour.approvalSubmittedAt ?: Instant.EPOCH
                }

    val pendingReviewTourItems: List<TourWithSession>
        get() =
            reviewTourItems.filter { tourWithSession ->
                tourWithSession.tour.approvalStatus == TourApprovalStatus.PENDING_REVIEW
            }

    val pastTourItems: List<TourWithSession>
        get() = pastTourItemsAt(Instant.now())

    fun activeTourItemsAt(now: Instant): List<TourWithSession> =
        sessions
            .mapNotNull { session -> findBySessionId(session.id) }
            .filter { tourWithSession ->
                tourWithSession.tour.approvalStatus == TourApprovalStatus.APPROVED &&
                    tourWithSession.session.status.canToggleBookingAvailability &&
                    !tourWithSession.session.isEffectivelyTerminal(now)
            }.sortedBy { it.session.startsAt }

    fun bookableTourItemsAt(now: Instant): List<TourWithSession> =
        activeTourItemsAt(now).filter { tourWithSession ->
            tourWithSession
                .resolveBookingAvailability(
                    hasReservation = false,
                    now = now,
                ).isBookable
        }

    fun bookableTourItemsForGuideAt(
        guideId: String,
        now: Instant,
    ): List<TourWithSession> =
        bookableTourItemsAt(now).filter { tourWithSession ->
            tourWithSession.tour.guide.id == guideId
        }

    fun pastTourItemsAt(now: Instant): List<TourWithSession> =
        sessions
            .mapNotNull { session -> findBySessionId(session.id) }
            .filter { tourWithSession -> tourWithSession.session.isEffectivelyTerminal(now) }
            .sortedByDescending { it.session.startsAt }

    fun findBySessionId(sessionId: String): TourWithSession? {
        val session = sessions.firstOrNull { it.id == sessionId } ?: return null
        val tour = tours.firstOrNull { it.id == session.tourId } ?: return null
        return TourWithSession(tour = tour, session = session)
    }
}
