package com.ahmetkaragunlu.guidemate.testing

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.reservation.domain.model.CancelReservationInput
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationResult
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationListType
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRefundEligibility
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationSnapshot
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.reservation.domain.repository.ReservationRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import java.time.Instant

class FakeReservationRepository : ReservationRepository {
    val reservationPages = ArrayDeque<DataResult<PagedResult<TouristReservation>>>()
    var reservationResult: DataResult<TouristReservation> =
        DataResult.Success(testReservation())
    var cancellationResult: DataResult<ReservationCancellationResult> =
        DataResult.Success(
            ReservationCancellationResult(
                reservation = testReservation(status = TouristReservationStatus.CANCELLED),
                refundEligibility = ReservationRefundEligibility.FULL_REFUND,
                refundId = "refund-1",
                refundStatus = null,
            )
        )
    val listRequests = mutableListOf<ReservationListType>()
    var cancellationRequest: Triple<String, CancelReservationInput, String>? = null

    override suspend fun getMyReservations(
        type: ReservationListType,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TouristReservation>> {
        listRequests += type
        return reservationPages.removeFirst()
    }

    override suspend fun getReservation(reservationId: String): DataResult<TouristReservation> =
        reservationResult

    override suspend fun cancelReservation(
        reservationId: String,
        input: CancelReservationInput,
        idempotencyKey: String,
    ): DataResult<ReservationCancellationResult> {
        cancellationRequest = Triple(reservationId, input, idempotencyKey)
        return cancellationResult
    }
}

fun testReservation(
    status: TouristReservationStatus = TouristReservationStatus.CONFIRMED,
): TouristReservation =
    TouristReservation(
        id = "reservation-1",
        tourSessionId = "session-1",
        version = 4,
        participantCount = 2,
        unitPriceMinor = 10_000,
        totalPriceMinor = 20_000,
        currencyCode = "USD",
        snapshot =
            TouristReservationSnapshot(
                tourId = "tour-1",
                guide = GuidePublicSummary(1L, "Ada Guide"),
                title = "City Walk",
                description = "Historic route",
                countryCode = "TR",
                country = "Turkiye",
                cityPlaceId = "istanbul-place-id",
                city = "Istanbul",
                timeZoneId = "UTC",
                category = TourCategory.CULTURE,
                languages = emptyList(),
                coverMediaId = "media-1",
                coverImageUrl = "https://example.com/tour.jpg",
                startsAt = Instant.parse("2099-01-01T12:00:00Z"),
                durationMinutes = 120,
                meetingPoint = "Main square",
                unitPriceMinor = 10_000,
            ),
        status = status,
        cancellationPolicyCode = "FULL_REFUND_48_HOURS",
        cancellationPolicyVersion = 1,
        averageRating = 4.8,
        reviewCount = 17,
        bookedCount = 6,
        capacity = 10,
    )

fun reservationPage(
    vararg reservations: TouristReservation,
): PagedResult<TouristReservation> =
    PagedResult(
        items = reservations.toList(),
        page = 0,
        size = 20,
        totalElements = reservations.size.toLong(),
        totalPages = if (reservations.isEmpty()) 0 else 1,
        isFirst = true,
        isLast = true,
    )
