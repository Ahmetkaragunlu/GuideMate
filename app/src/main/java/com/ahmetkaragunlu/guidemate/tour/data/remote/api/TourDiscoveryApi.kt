package com.ahmetkaragunlu.guidemate.tour.data.remote.api

import com.ahmetkaragunlu.guidemate.common.network.model.response.ApiPageResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.response.TourDetailResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.response.TourSearchItemResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TourDiscoveryApi {
    @GET("api/v1/tours/search")
    suspend fun searchTours(
        @Query("q") query: String?,
        @Query("countryCode") countryCode: String?,
        @Query("cityPlaceId") cityPlaceId: String?,
        @Query("categoryCode") categoryCode: String?,
        @Query("languageCodes") languageCodes: List<String>?,
        @Query("minRating") minimumRating: Double?,
        @Query("minPriceMinor") minimumPriceMinor: Long?,
        @Query("maxPriceMinor") maximumPriceMinor: Long?,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<ApiPageResponse<TourSearchItemResponse>>

    @GET("api/v1/tours/popular")
    suspend fun getPopularTours(
        @Query("guideId") guideId: Long?,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<TourSearchItemResponse>>

    @GET("api/v1/tours/{tourId}")
    suspend fun getTour(
        @Path("tourId") tourId: String,
    ): Response<TourDetailResponse>

    @GET("api/v1/tour-sessions/{sessionId}")
    suspend fun getSession(
        @Path("sessionId") sessionId: String,
    ): Response<TourDetailResponse>
}
