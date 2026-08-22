package com.ahmetkaragunlu.guidemate.tour.data.remote.api

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.CancelTourSessionRequestDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.CreateGuideTourRequestDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.GuideDashboardResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.GuideTourCardResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.SubmitTourChangeRequestDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourDetailResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourReviewSubmissionResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourSessionRequestDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourSessionResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.UpdateTourSessionRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GuideTourApi {
    @GET("api/v1/guide/tours")
    suspend fun getTours(
        @Query("tab") tab: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<GuideTourCardResponseDto>>

    @GET("api/v1/guide/tours/{tourId}")
    suspend fun getTour(
        @Path("tourId") tourId: String,
    ): Response<TourDetailResponseDto>

    @POST("api/v1/guide/tours")
    suspend fun createTour(
        @Body request: CreateGuideTourRequestDto,
    ): Response<TourReviewSubmissionResponseDto>

    @POST("api/v1/guide/tours/{tourId}/change-requests")
    suspend fun submitChange(
        @Path("tourId") tourId: String,
        @Body request: SubmitTourChangeRequestDto,
    ): Response<TourReviewSubmissionResponseDto>

    @POST("api/v1/guide/tours/{tourId}/sessions")
    suspend fun addSession(
        @Path("tourId") tourId: String,
        @Body request: TourSessionRequestDto,
    ): Response<TourSessionResponseDto>

    @PATCH("api/v1/guide/sessions/{sessionId}")
    suspend fun updateSession(
        @Path("sessionId") sessionId: String,
        @Body request: UpdateTourSessionRequestDto,
    ): Response<TourSessionResponseDto>

    @POST("api/v1/guide/sessions/{sessionId}/open")
    suspend fun openSession(
        @Path("sessionId") sessionId: String,
    ): Response<TourSessionResponseDto>

    @POST("api/v1/guide/sessions/{sessionId}/close")
    suspend fun closeSession(
        @Path("sessionId") sessionId: String,
    ): Response<TourSessionResponseDto>

    @POST("api/v1/guide/sessions/{sessionId}/cancel")
    suspend fun cancelSession(
        @Path("sessionId") sessionId: String,
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body request: CancelTourSessionRequestDto,
    ): Response<TourSessionResponseDto>

    @POST("api/v1/guide/tours/{tourId}/archive")
    suspend fun archiveTour(
        @Path("tourId") tourId: String,
    ): Response<TourDetailResponseDto>

    @GET("api/v1/guides/me/dashboard")
    suspend fun getDashboard(): Response<GuideDashboardResponseDto>
}
