package com.ahmetkaragunlu.guidemate.review.data.remote.api

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.review.data.remote.model.ReviewSubmissionRequestDto
import com.ahmetkaragunlu.guidemate.review.data.remote.model.SubmittedReviewResponseDto
import com.ahmetkaragunlu.guidemate.review.data.remote.model.TourReviewResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Query

interface ReviewApi {
    @POST("api/v1/reservations/{reservationId}/reviews")
    suspend fun submitReview(
        @Path("reservationId") reservationId: String,
        @Body request: ReviewSubmissionRequestDto,
    ): Response<SubmittedReviewResponseDto>

    @GET("api/v1/tours/{tourId}/reviews")
    suspend fun getTourReviews(
        @Path("tourId") tourId: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<TourReviewResponseDto>>
}
