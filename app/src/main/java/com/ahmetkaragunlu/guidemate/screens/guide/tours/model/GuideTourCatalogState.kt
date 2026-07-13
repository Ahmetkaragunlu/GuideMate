package com.ahmetkaragunlu.guidemate.screens.guide.tours.model

import java.time.Instant

data class GuideTourCatalogState(
    val tours: List<Tour> = emptyList(),
    val sessions: List<TourSession> = emptyList(),
) {
    val activeTourItems: List<TourWithSession>
        get() =
            sessions
                .mapNotNull { session -> findBySessionId(session.id) }
                .filter { tourWithSession ->
                    tourWithSession.tour.approvalStatus == TourApprovalStatus.APPROVED &&
                        tourWithSession.session.status.isBookingManageable
                }.sortedBy { it.session.startsAt }

    val publishedTourItems: List<TourWithSession>
        get() =
            activeTourItems.filter { tourWithSession ->
                tourWithSession.session.status == TourSessionStatus.OPEN_FOR_BOOKING
            }

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
                    tourWithSession.tour.reviewSubmittedAt ?: Instant.EPOCH
                }

    val pendingReviewTourItems: List<TourWithSession>
        get() =
            reviewTourItems.filter { tourWithSession ->
                tourWithSession.tour.approvalStatus == TourApprovalStatus.PENDING_REVIEW
            }

    val pastTourItems: List<TourWithSession>
        get() =
            sessions
                .mapNotNull { session -> findBySessionId(session.id) }
                .filter { tourWithSession -> tourWithSession.session.status.isTerminal }
                .sortedByDescending { it.session.startsAt }

    fun findBySessionId(sessionId: String): TourWithSession? {
        val session = sessions.firstOrNull { it.id == sessionId } ?: return null
        val tour = tours.firstOrNull { it.id == session.tourId } ?: return null
        return TourWithSession(tour = tour, session = session)
    }
}
