package com.ahmetkaragunlu.guidemate.reservation.data.repository

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.network.testApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.reservation.data.remote.api.ReservationApi
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.CancelReservationRequestDto
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.ReservationCancellationResponseDto
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.ReservationGuideResponseDto
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.ReservationResponseDto
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.ReservationSnapshotResponseDto
import com.ahmetkaragunlu.guidemate.reservation.domain.model.CancelReservationInput
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationListType
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRefundEligibility
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRefundStatus
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class ReservationRepositoryImplTest {
    @Test
    fun `list forwards backend status and pagination`() = runBlocking {
        val api = FakeReservationApi()
        val repository = ReservationRepositoryImpl(api, testApiCallExecutor())

        val result = repository.getMyReservations(ReservationListType.PAST, page = 2, size = 15)

        assertTrue(result.toString(), result is DataResult.Success)
        assertEquals("PAST", api.listStatus)
        assertEquals(2, api.listPage)
        assertEquals(15, api.listSize)
        val reservation = (result as DataResult.Success).data.items.single()
        assertEquals("reservation-1", reservation.id)
        assertEquals(ReservationRefundEligibility.NOT_APPLICABLE, reservation.refundEligibility)
    }

    @Test
    fun `cancel forwards optimistic version reason and idempotency key`() = runBlocking {
        val api = FakeReservationApi()
        val repository = ReservationRepositoryImpl(api, testApiCallExecutor())

        val result =
            repository.cancelReservation(
                reservationId = "reservation-1",
                input = CancelReservationInput(version = 4, reason = "  Plan changed  "),
                idempotencyKey = "cancel-key-1",
            )

        assertTrue(result.toString(), result is DataResult.Success)
        assertEquals("reservation-1", api.cancelReservationId)
        assertEquals("cancel-key-1", api.cancelIdempotencyKey)
        assertEquals(4L, api.cancelRequest?.version)
        assertEquals("Plan changed", api.cancelRequest?.reason)
        assertEquals(
            ReservationRefundStatus.REQUESTED,
            (result as DataResult.Success).data.refundStatus,
        )
        assertEquals(
            ReservationRefundEligibility.FULL_REFUND,
            result.data.reservation.refundEligibility,
        )
    }

    private class FakeReservationApi : ReservationApi {
        var listStatus: String? = null
        var listPage: Int? = null
        var listSize: Int? = null
        var cancelReservationId: String? = null
        var cancelIdempotencyKey: String? = null
        var cancelRequest: CancelReservationRequestDto? = null

        override suspend fun getMyReservations(
            status: String,
            page: Int,
            size: Int,
        ): Response<ApiPageResponse<ReservationResponseDto>> {
            listStatus = status
            listPage = page
            listSize = size
            return Response.success(
                ApiPageResponse(
                    content = listOf(reservation()),
                    page = page,
                    size = size,
                    totalElements = 1,
                    totalPages = 1,
                    isFirst = page == 0,
                    isLast = true,
                ),
            )
        }

        override suspend fun getReservation(
            reservationId: String,
        ): Response<ReservationResponseDto> = Response.success(reservation())

        override suspend fun cancelReservation(
            reservationId: String,
            idempotencyKey: String,
            request: CancelReservationRequestDto,
        ): Response<ReservationCancellationResponseDto> {
            cancelReservationId = reservationId
            cancelIdempotencyKey = idempotencyKey
            cancelRequest = request
            return Response.success(
                ReservationCancellationResponseDto(
                    reservation =
                        reservation().copy(
                            status = "CANCELLED",
                            cancellationActor = "TOURIST",
                            cancellationReason = request.reason,
                            cancelledAt = "2026-08-25T12:00:00Z",
                            cancellationRefundEligibility = "FULL_REFUND",
                        ),
                    refundEligibility = "FULL_REFUND",
                    refundId = "refund-1",
                    refundStatus = "REQUESTED",
                ),
            )
        }

        private fun reservation(): ReservationResponseDto =
            ReservationResponseDto(
                reservationId = "reservation-1",
                sessionId = "session-1",
                version = 4,
                participantCount = 2,
                unitPriceMinor = 5_000,
                totalPriceMinor = 10_000,
                currencyCode = "USD",
                status = "CONFIRMED",
                holdExpiresAt = null,
                cancellationActor = null,
                cancellationReason = null,
                cancelledAt = null,
                cancellationRefundEligibility = null,
                cancellationPolicyCode = "STANDARD",
                cancellationPolicyVersion = 1,
                snapshot =
                    ReservationSnapshotResponseDto(
                        tourId = "tour-1",
                        guide =
                            ReservationGuideResponseDto(
                                guideId = 9,
                                displayName = "Guide Name",
                                avatar = null,
                            ),
                        title = "City Walk",
                        description = "Description",
                        countryCode = "TR",
                        cityPlaceId = "city-1",
                        cityName = "Istanbul",
                        timeZoneId = "Europe/Istanbul",
                        categoryCode = "CULTURE",
                        languageCodes = emptyList(),
                        cover = null,
                        startsAt = "2026-09-01T10:00:00Z",
                        durationMinutes = 120,
                        meetingPoint = "Square",
                        unitPriceMinor = 5_000,
                    ),
                review = null,
            )
    }
}
