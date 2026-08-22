package com.ahmetkaragunlu.guidemate.reservation.data.repository

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.AppError
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
import kotlinx.coroutines.CancellationException
import retrofit2.Response

class ReservationRepositoryImpl @Inject constructor(
    private val api: ReservationApi,
    private val apiErrorParser: ApiErrorParser,
    private val networkExceptionMapper: NetworkExceptionMapper,
) : ReservationRepository {
    override suspend fun getMyReservations(
        type: ReservationListType,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TouristReservation>> =
        execute(
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
        execute(
            request = { api.getReservation(reservationId) },
            transform = { it.toDomain() },
        )

    override suspend fun cancelReservation(
        reservationId: String,
        input: CancelReservationInput,
        idempotencyKey: String,
    ): DataResult<ReservationCancellationResult> =
        execute(
            request = {
                api.cancelReservation(
                    reservationId = reservationId,
                    idempotencyKey = idempotencyKey,
                    request = input.toDto(),
                )
            },
            transform = { it.toDomain() },
        )

    private suspend fun <ResponseBody, Domain> execute(
        request: suspend () -> Response<ResponseBody>,
        transform: (ResponseBody) -> Domain,
    ): DataResult<Domain> =
        try {
            val response = request()
            if (!response.isSuccessful) {
                DataResult.Error(apiErrorParser.parse(response))
            } else {
                response.body()?.let { DataResult.Success(transform(it)) }
                    ?: DataResult.Error(AppError.NoResponseFromServer)
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            DataResult.Error(networkExceptionMapper.map(exception), exception)
        }
}
