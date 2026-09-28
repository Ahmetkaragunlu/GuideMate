package com.ahmetkaragunlu.guidemate.profile.data.remote.api

import com.ahmetkaragunlu.guidemate.common.network.model.response.ApiPageResponse
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.response.GuideProfileResponse
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.response.GuideSearchItemResponse
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.request.UpdateGuideProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface GuideProfileApi {
    @GET("api/v1/guides/me/profile")
    suspend fun getOwnProfile(): Response<GuideProfileResponse>

    @PATCH("api/v1/guides/me/profile")
    suspend fun updateOwnProfile(
        @Body request: UpdateGuideProfileRequest,
    ): Response<GuideProfileResponse>

    @GET("api/v1/guides/{guideId}/public-profile")
    suspend fun getPublicProfile(
        @Path("guideId") guideId: Long,
    ): Response<GuideProfileResponse>

    @GET("api/v1/guides/search")
    suspend fun searchGuides(
        @Query("q") query: String?,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<GuideSearchItemResponse>>

    @GET("api/v1/guides/top")
    suspend fun getTopGuides(
        @Query("limit") limit: Int,
    ): Response<List<GuideSearchItemResponse>>
}
