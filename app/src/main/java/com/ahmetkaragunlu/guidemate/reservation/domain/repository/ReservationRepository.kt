package com.ahmetkaragunlu.guidemate.reservation.domain.repository

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.reservation.domain.model.CancelReservationInput
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationResult
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationListType
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation

interface ReservationRepository {
    suspend fun getMyReservations(
        type: ReservationListType,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TouristReservation>>

    suspend fun getReservation(reservationId: String): DataResult<TouristReservation>

    suspend fun cancelReservation(
        reservationId: String,
        input: CancelReservationInput,
        idempotencyKey: String,
    ): DataResult<ReservationCancellationResult>
}
