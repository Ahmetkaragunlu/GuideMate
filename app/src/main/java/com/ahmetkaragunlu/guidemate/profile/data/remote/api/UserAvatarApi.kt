package com.ahmetkaragunlu.guidemate.profile.data.remote.api

import com.ahmetkaragunlu.guidemate.media.data.remote.model.response.MediaReferenceResponse
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.request.UpdateUserAvatarRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface UserAvatarApi {
    @PUT("api/v1/users/me/avatar")
    suspend fun updateAvatar(
        @Body request: UpdateUserAvatarRequest,
    ): Response<MediaReferenceResponse>
}
