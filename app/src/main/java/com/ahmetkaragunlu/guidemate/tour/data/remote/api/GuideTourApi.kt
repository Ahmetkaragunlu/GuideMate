package com.ahmetkaragunlu.guidemate.tour.data.remote.api

import com.ahmetkaragunlu.guidemate.common.network.model.response.ApiPageResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.request.CancelTourSessionRequest
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.request.CreateGuideTourRequest
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.response.GuideDashboardResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.response.GuideTourCardResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.request.SubmitTourChangeRequest
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.response.TourDetailResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.response.TourReviewSubmissionResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.request.TourSessionRequest
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.response.TourSessionResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.request.UpdateTourSessionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GuideTourApi {
    @GET("api/v1/guides/me/tours")
    suspend fun getTours(
        @Query("tab") tab: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<GuideTourCardResponse>>

    @GET("api/v1/guides/me/tours/{tourId}")
    suspend fun getTour(
        @Path("tourId") tourId: String,
    ): Response<TourDetailResponse>

    @POST("api/v1/guides/me/tours")
    suspend fun createTour(
        @Body request: CreateGuideTourRequest,
    ): Response<TourReviewSubmissionResponse>

    @POST("api/v1/guides/me/tours/{tourId}/change-requests")
    suspend fun submitChange(
        @Path("tourId") tourId: String,
        @Body request: SubmitTourChangeRequest,
    ): Response<TourReviewSubmissionResponse>

    @POST("api/v1/guides/me/tours/{tourId}/sessions")
    suspend fun addSession(
        @Path("tourId") tourId: String,
        @Body request: TourSessionRequest,
    ): Response<TourSessionResponse>

    @PATCH("api/v1/guides/me/sessions/{sessionId}")
    suspend fun updateSession(
        @Path("sessionId") sessionId: String,
        @Body request: UpdateTourSessionRequest,
    ): Response<TourSessionResponse>

    @POST("api/v1/guides/me/sessions/{sessionId}/open")
    suspend fun openSession(
        @Path("sessionId") sessionId: String,
    ): Response<TourSessionResponse>

    @POST("api/v1/guides/me/sessions/{sessionId}/close")
    suspend fun closeSession(
        @Path("sessionId") sessionId: String,
    ): Response<TourSessionResponse>

    @POST("api/v1/guides/me/sessions/{sessionId}/cancel")
    suspend fun cancelSession(
        @Path("sessionId") sessionId: String,
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body request: CancelTourSessionRequest,
    ): Response<TourSessionResponse>

    @POST("api/v1/guides/me/tours/{tourId}/archive")
    suspend fun archiveTour(
        @Path("tourId") tourId: String,
    ): Response<TourDetailResponse>

    @GET("api/v1/guides/me/dashboard")
    suspend fun getDashboard(): Response<GuideDashboardResponse>
}
