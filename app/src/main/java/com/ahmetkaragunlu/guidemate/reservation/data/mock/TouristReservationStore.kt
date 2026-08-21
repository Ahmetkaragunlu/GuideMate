package com.ahmetkaragunlu.guidemate.reservation.data.mock

import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourCatalogState
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import com.ahmetkaragunlu.guidemate.tour.data.mock.TourCatalogStore
import com.ahmetkaragunlu.guidemate.reservation.domain.model.CreateTourReviewRequest
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationReview
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationSnapshot
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class TouristReservationStore
    @Inject
    constructor(
        tourCatalogStore: TourCatalogStore,
    ) {
        private val _reservations =
            MutableStateFlow(createMockReservations(tourCatalogStore.state.value))
        val reservations: StateFlow<List<TouristReservation>> = _reservations.asStateFlow()

        fun cancelReservation(reservationId: String): Boolean {
            val reservation =
                reservations.value.firstOrNull { it.id == reservationId }
                    ?: return false
            if (reservation.status != TouristReservationStatus.CONFIRMED) return false

            _reservations.update { reservations ->
                reservations.map { reservation ->
                    if (reservation.id == reservationId) {
                        reservation.copy(status = TouristReservationStatus.CANCELLED)
                    } else {
                        reservation
                    }
                }
            }
            return true
        }

        fun submitReview(request: CreateTourReviewRequest): Boolean {
            if (request.rating !in 1..5) return false

            var submitted = false
            _reservations.update { reservations ->
                reservations.map { reservation ->
                    if (
                        reservation.id == request.reservationId &&
                        reservation.status == TouristReservationStatus.CONFIRMED &&
                        reservation.review == null
                    ) {
                        submitted = true
                        reservation.copy(
                            review =
                                ReservationReview(
                                    id = UUID.randomUUID().toString(),
                                    rating = request.rating,
                                    comment = request.comment.trim(),
                                    submittedAt = Instant.now(),
                                ),
                        )
                    } else {
                        reservation
                    }
                }
            }
            return submitted
        }

        private companion object {
            private data class MockReservationSeed(
                val reservationId: String,
                val sessionId: String,
                val participantCount: Int,
            )

            private val mockReservationSeeds =
                listOf(
                    MockReservationSeed(
                        reservationId = "reservation-kapadokya",
                        sessionId = "session-kapadokya-active",
                        participantCount = 2,
                    ),
                    MockReservationSeed(
                        reservationId = "reservation-efes",
                        sessionId = "session-efes-active",
                        participantCount = 1,
                    ),
                    MockReservationSeed(
                        reservationId = "reservation-bogaz",
                        sessionId = "session-bogaz-past",
                        participantCount = 2,
                    ),
                    MockReservationSeed(
                        reservationId = "reservation-pamukkale",
                        sessionId = "session-pamukkale-cancelled",
                        participantCount = 1,
                    ),
                )

            fun createMockReservations(catalog: TourCatalogState): List<TouristReservation> =
                mockReservationSeeds.mapNotNull { seed ->
                    catalog
                        .findBySessionId(seed.sessionId)
                        ?.toMockReservation(
                            reservationId = seed.reservationId,
                            participantCount = seed.participantCount,
                        )
                }

            private fun TourWithSession.toMockReservation(
                reservationId: String,
                participantCount: Int,
            ): TouristReservation =
                TouristReservation(
                    id = reservationId,
                    tourSessionId = session.id,
                    participantCount = participantCount,
                    status =
                        if (session.status == TourSessionStatus.CANCELLED) {
                            TouristReservationStatus.CANCELLED
                        } else {
                            TouristReservationStatus.CONFIRMED
                        },
                    snapshot =
                        TouristReservationSnapshot(
                            tourId = tour.id,
                            guide = tour.guide,
                            title = tour.title,
                            description = tour.description,
                            country = tour.country,
                            city = tour.city,
                            timeZoneId = tour.timeZoneId,
                            category = tour.category,
                            languages = tour.languages,
                            coverImageResId = tour.coverImageResId,
                            coverImageUrl = tour.coverImageUrl,
                            startsAt = session.startsAt,
                            durationMinutes = session.durationMinutes,
                            meetingPoint = session.meetingPoint,
                            unitPriceMinor = session.priceMinor,
                        ),
                )
        }
    }
