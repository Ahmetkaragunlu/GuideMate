package com.ahmetkaragunlu.guidemate.reservation.data.repository

import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.reservation.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.reservation.data.mapper.toDto
import com.ahmetkaragunlu.guidemate.reservation.data.remote.api.ReservationApi
import com.ahmetkaragunlu.guidemate.reservation.domain.model.CancelReservationInput
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationResult
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationListType
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation
import com.ahmetkaragunlu.guidemate.reservation.domain.repository.ReservationRepository
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val api: ReservationApi,
    private val apiCallExecutor: ApiCallExecutor,
) : ReservationRepository {
    override suspend fun getMyReservations(
        type: ReservationListType,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TouristReservation>> =
        apiCallExecutor.execute(
            request = {
                api.getMyReservations(
                    status = type.name,
                    page = page,
                    size = size,
                )
            },
            transform = { it.toDomain() },
        )

    override suspend fun getReservation(reservationId: String): DataResult<TouristReservation> =
        apiCallExecutor.execute(
            request = { api.getReservation(reservationId) },
            transform = { it.toDomain() },
        )

    override suspend fun cancelReservation(
        reservationId: String,
        input: CancelReservationInput,
        idempotencyKey: String,
    ): DataResult<ReservationCancellationResult> =
        apiCallExecutor.execute(
            request = {
                api.cancelReservation(
                    reservationId = reservationId,
                    idempotencyKey = idempotencyKey,
                    request = input.toDto(),
                )
            },
            transform = { it.toDomain() },
        )
}
