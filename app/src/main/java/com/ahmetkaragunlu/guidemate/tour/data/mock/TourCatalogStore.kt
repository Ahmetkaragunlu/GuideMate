package com.ahmetkaragunlu.guidemate.tour.data.mock

import com.ahmetkaragunlu.guidemate.tour.domain.model.Tour
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourCatalogState
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.CancelTourSessionRequest
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.CreateTourSessionRequest
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourEditRequest
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourOperationResult
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class TourCatalogStore
    @Inject
    constructor() {
        private val _state = MutableStateFlow(createMockTourCatalogState())
        val state: StateFlow<TourCatalogState> = _state.asStateFlow()

        fun submitForReview(
            tour: Tour,
            session: TourSession,
        ): Boolean {
            val current = _state.value
            val now = Instant.now()
            if (
                tour.id != session.tourId ||
                    current.tours.any { it.id == tour.id } ||
                    current.sessions.any { it.id == session.id } ||
                    tour.title.isBlank() ||
                    tour.description.isBlank() ||
                    session.meetingPoint.isBlank() ||
                    tour.country.isBlank() ||
                    tour.city.isBlank() ||
                    tour.languages.isEmpty() ||
                    tour.approvalStatus != TourApprovalStatus.PENDING_REVIEW ||
                    tour.publishedAt != null ||
                    session.status != TourSessionStatus.CLOSED ||
                    session.bookedCount != 0 ||
                    !session.startsAt.isAfter(now) ||
                    session.durationMinutes <= 0 ||
                    session.priceMinor <= 0 ||
                    session.capacity <= 0
            ) {
                return false
            }

            val guideTourIds = current.guideTourIds(tour.guide.id)
            val overlaps =
                current.sessions.any { existingSession ->
                    existingSession.tourId in guideTourIds &&
                        current.isScheduleBlocking(existingSession) &&
                        session.startsAt < existingSession.endsAt &&
                        session.endsAt > existingSession.startsAt
                }
            if (overlaps) return false

            _state.update { current ->
                current.copy(
                    tours = current.tours + tour.copy(approvalSubmittedAt = now),
                    sessions = current.sessions + session,
                )
            }
            return true
        }

        fun setSessionBookingOpen(
            sessionId: String,
            isOpen: Boolean,
            now: Instant = Instant.now(),
        ): TourOperationResult {
            val current =
                _state.value.findBySessionId(sessionId)
                    ?: return TourOperationResult.SESSION_NOT_FOUND
            if (!current.session.startsAt.isAfter(now)) {
                return TourOperationResult.SESSION_ALREADY_STARTED
            }
            if (!current.session.status.canToggleBookingAvailability) {
                return TourOperationResult.STATUS_NOT_MANAGEABLE
            }
            if (isOpen && current.tour.approvalStatus != TourApprovalStatus.APPROVED) {
                return TourOperationResult.TOUR_NOT_APPROVED
            }
            if (isOpen && current.session.bookedCount >= current.session.capacity) {
                return TourOperationResult.CAPACITY_FULL
            }

            updateSession(sessionId) { session ->
                session.copy(
                    status =
                        if (isOpen) {
                            TourSessionStatus.OPEN_FOR_BOOKING
                        } else {
                            TourSessionStatus.CLOSED
                        },
                )
            }
            return TourOperationResult.SUCCESS
        }

        fun updateTour(
            tourId: String,
            sessionId: String,
            request: TourEditRequest,
        ): Boolean {
            val catalog = _state.value
            val current = catalog.findBySessionId(sessionId) ?: return false
            val now = Instant.now()
            if (current.tour.id != tourId) return false
            if (!current.session.status.canToggleBookingAvailability) return false

            val locationChanged =
                request.country.trim() != current.tour.country ||
                    request.city.trim() != current.tour.city
            val categoryChanged = request.category != current.tour.category
            if (current.tour.publishedAt != null && locationChanged) return false
            if (
                current.session.bookedCount > 0 &&
                    (
                        request.startsAt != current.session.startsAt ||
                            request.durationMinutes != current.session.durationMinutes ||
                            request.meetingPoint.trim() != current.session.meetingPoint
                    )
            ) {
                return false
            }
            if (
                request.title.isBlank() ||
                    request.description.isBlank() ||
                    request.country.isBlank() ||
                    request.city.isBlank() ||
                    request.languages.isEmpty() ||
                    request.meetingPoint.isBlank() ||
                    !request.startsAt.isAfter(now) ||
                    request.durationMinutes <= 0 ||
                    request.priceMinor <= 0 ||
                    request.capacity <= 0
            ) {
                return false
            }
            if (request.capacity < current.session.bookedCount) return false

            val requestEndsAt = request.startsAt.plusSeconds(request.durationMinutes * 60L)
            val guideTourIds = catalog.guideTourIds(current.tour.guide.id)
            val overlapsAnotherSession =
                catalog.sessions.any { session ->
                    session.id != sessionId &&
                        session.tourId in guideTourIds &&
                        catalog.isScheduleBlocking(session) &&
                        request.startsAt < session.endsAt &&
                        requestEndsAt > session.startsAt
                }
            if (overlapsAnotherSession) return false

            val requiresReview =
                current.tour.approvalStatus != TourApprovalStatus.APPROVED ||
                    request.title.trim() != current.tour.title ||
                    request.description.trim() != current.tour.description ||
                    categoryChanged ||
                    request.languages != current.tour.languages ||
                    request.selectedCoverImageUri != null

            _state.update { state ->
                state.copy(
                    tours =
                        state.tours.map { tour ->
                            if (tour.id == tourId) {
                                tour.copy(
                                    title = request.title.trim(),
                                    description = request.description.trim(),
                                    country = request.country.trim(),
                                    city = request.city.trim(),
                                    category = request.category,
                                    languages = request.languages,
                                    coverImageUrl =
                                        request.selectedCoverImageUri ?: tour.coverImageUrl,
                                    approvalStatus =
                                        if (requiresReview) {
                                            TourApprovalStatus.PENDING_REVIEW
                                        } else {
                                            tour.approvalStatus
                                        },
                                    rejectionReason = if (requiresReview) null else tour.rejectionReason,
                                    approvalSubmittedAt =
                                        if (requiresReview) {
                                            now
                                        } else {
                                            tour.approvalSubmittedAt
                                        },
                                )
                            } else {
                                tour
                            }
                        },
                    sessions =
                        state.sessions.map { session ->
                            if (session.id == sessionId) {
                                session.copy(
                                    meetingPoint = request.meetingPoint.trim(),
                                    startsAt = request.startsAt,
                                    durationMinutes = request.durationMinutes,
                                    priceMinor = request.priceMinor,
                                    capacity = request.capacity,
                                    status =
                                        if (requiresReview) {
                                            TourSessionStatus.CLOSED
                                        } else {
                                            session.status
                                        },
                                )
                            } else if (
                                requiresReview &&
                                    session.tourId == tourId &&
                                    session.startsAt.isAfter(now) &&
                                    session.status.canToggleBookingAvailability
                            ) {
                                session.copy(status = TourSessionStatus.CLOSED)
                            } else {
                                session
                            }
                        },
                )
            }
            return true
        }

        fun archiveRejectedTour(tourId: String): TourOperationResult {
            val current = _state.value
            val tour =
                current.tours.firstOrNull { it.id == tourId }
                    ?: return TourOperationResult.TOUR_NOT_FOUND
            if (
                tour.approvalStatus != TourApprovalStatus.REJECTED ||
                    tour.publishedAt != null
            ) {
                return TourOperationResult.TOUR_NOT_ARCHIVABLE
            }

            _state.update { state ->
                state.copy(
                    tours =
                        state.tours.map { item ->
                            if (item.id == tourId) {
                                item.copy(approvalStatus = TourApprovalStatus.ARCHIVED)
                            } else {
                                item
                            }
                        },
                    sessions =
                        state.sessions.map { session ->
                            if (session.tourId == tourId && session.status.canToggleBookingAvailability) {
                                session.copy(status = TourSessionStatus.CLOSED)
                            } else {
                                session
                            }
                        },
                )
            }
            return TourOperationResult.SUCCESS
        }

        fun cancelSession(request: CancelTourSessionRequest): Boolean {
            val current = _state.value.findBySessionId(request.sessionId) ?: return false
            if (
                !current.session.status.canToggleBookingAvailability ||
                    !current.session.startsAt.isAfter(Instant.now()) ||
                    request.reason.isBlank()
            ) {
                return false
            }

            updateSession(request.sessionId) { session ->
                session.copy(
                    status = TourSessionStatus.CANCELLED,
                    cancellationReason = request.reason.trim(),
                )
            }
            return true
        }

        fun addSession(request: CreateTourSessionRequest): Boolean {
            val state = _state.value
            val now = Instant.now()
            val tour = state.tours.firstOrNull { it.id == request.tourId } ?: return false
            if (tour.approvalStatus != TourApprovalStatus.APPROVED) return false
            if (
                request.meetingPoint.isBlank() ||
                !request.startsAt.isAfter(now) ||
                    request.durationMinutes <= 0 ||
                    request.priceMinor <= 0 ||
                    request.capacity <= 0
            ) {
                return false
            }

            val endsAt = request.startsAt.plusSeconds(request.durationMinutes * 60L)
            val guideTourIds = state.guideTourIds(tour.guide.id)
            val overlaps =
                state.sessions.any { session ->
                    session.tourId in guideTourIds &&
                        state.isScheduleBlocking(session) &&
                        request.startsAt < session.endsAt &&
                        endsAt > session.startsAt
                }
            if (overlaps) return false

            val session =
                TourSession(
                    id = UUID.randomUUID().toString(),
                    tourId = request.tourId,
                    meetingPoint = request.meetingPoint.trim(),
                    startsAt = request.startsAt,
                    durationMinutes = request.durationMinutes,
                    priceMinor = request.priceMinor,
                    capacity = request.capacity,
                    bookedCount = 0,
                    status = TourSessionStatus.OPEN_FOR_BOOKING,
                )
            _state.update { current -> current.copy(sessions = current.sessions + session) }
            return true
        }

        private fun TourCatalogState.guideTourIds(guideId: String): Set<String> =
            tours.filter { it.guide.id == guideId }.mapTo(mutableSetOf()) { it.id }

        private fun TourCatalogState.isScheduleBlocking(session: TourSession): Boolean {
            val approvalStatus = tours.firstOrNull { it.id == session.tourId }?.approvalStatus ?: return false
            return !session.status.isTerminal &&
                approvalStatus != TourApprovalStatus.REJECTED &&
                approvalStatus != TourApprovalStatus.ARCHIVED
        }

        private fun updateSession(
            sessionId: String,
            transform: (TourSession) -> TourSession,
        ) {
            _state.update { current ->
                current.copy(
                    sessions = current.sessions.map { session -> if (session.id == sessionId) transform(session) else session },
                )
            }
        }
    }
