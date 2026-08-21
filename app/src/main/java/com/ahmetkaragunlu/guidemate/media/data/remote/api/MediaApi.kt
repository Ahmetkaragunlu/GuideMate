package com.ahmetkaragunlu.guidemate.media.data.remote.api

import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaDeletionResponse
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaUploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaApi {
    @Multipart
    @POST("api/v1/media")
    suspend fun upload(
        @Part file: MultipartBody.Part,
        @Query("purpose") purpose: String,
    ): Response<MediaUploadResponse>

    @DELETE("api/v1/media/{mediaId}")
    suspend fun delete(
        @Path("mediaId") mediaAssetId: String,
    ): Response<MediaDeletionResponse>
}
