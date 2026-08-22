package com.ahmetkaragunlu.guidemate.reservation.data.remote.api

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.CancelReservationRequestDto
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.ReservationCancellationResponseDto
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.ReservationResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReservationApi {
    @GET("api/v1/reservations/me")
    suspend fun getMyReservations(
        @Query("status") status: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<ReservationResponseDto>>

    @GET("api/v1/reservations/{reservationId}")
    suspend fun getReservation(
        @Path("reservationId") reservationId: String,
    ): Response<ReservationResponseDto>

    @POST("api/v1/reservations/{reservationId}/cancel")
    suspend fun cancelReservation(
        @Path("reservationId") reservationId: String,
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body request: CancelReservationRequestDto,
    ): Response<ReservationCancellationResponseDto>
}
