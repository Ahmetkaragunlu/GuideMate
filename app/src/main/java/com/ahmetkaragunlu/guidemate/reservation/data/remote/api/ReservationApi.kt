package com.ahmetkaragunlu.guidemate.reservation.data.remote.api

import com.ahmetkaragunlu.guidemate.common.network.model.response.ApiPageResponse
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.request.CancelReservationRequest
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.response.ReservationCancellationResponse
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.response.ReservationResponse
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
    ): Response<ApiPageResponse<ReservationResponse>>

    @GET("api/v1/reservations/{reservationId}")
    suspend fun getReservation(
        @Path("reservationId") reservationId: String,
    ): Response<ReservationResponse>

    @POST("api/v1/reservations/{reservationId}/cancel")
    suspend fun cancelReservation(
        @Path("reservationId") reservationId: String,
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body request: CancelReservationRequest,
    ): Response<ReservationCancellationResponse>
}
