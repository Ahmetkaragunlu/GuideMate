package com.ahmetkaragunlu.guidemate.notification.data.remote.api

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.MarkRelatedNotificationsReadRequestDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.NotificationPreferencesResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.NotificationResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.RegisterDeviceRequestDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.UnreadCountResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.UpdateNotificationPreferencesRequestDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {
    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<NotificationResponseDto>>

    @GET("api/v1/notifications/unread-count")
    suspend fun getUnreadCount(): Response<UnreadCountResponseDto>

    @POST("api/v1/notifications/{notificationId}/read")
    suspend fun markRead(
        @Path("notificationId") notificationId: String,
    ): Response<NotificationResponseDto>

    @POST("api/v1/notifications/read-all")
    suspend fun markAllRead(): Response<UnreadCountResponseDto>

    @POST("api/v1/notifications/read-related")
    suspend fun markRelatedRead(
        @Body request: MarkRelatedNotificationsReadRequestDto,
    ): Response<UnreadCountResponseDto>

    @GET("api/v1/notifications/preferences")
    suspend fun getPreferences(): Response<NotificationPreferencesResponseDto>

    @PATCH("api/v1/notifications/preferences")
    suspend fun updatePreferences(
        @Body request: UpdateNotificationPreferencesRequestDto,
    ): Response<NotificationPreferencesResponseDto>

    @POST("api/v1/devices/fcm-registration")
    suspend fun registerDevice(
        @Body request: RegisterDeviceRequestDto,
    ): Response<ResponseBody>
}
