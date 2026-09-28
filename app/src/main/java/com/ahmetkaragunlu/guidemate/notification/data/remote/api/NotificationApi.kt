package com.ahmetkaragunlu.guidemate.notification.data.remote.api

import com.ahmetkaragunlu.guidemate.common.network.model.response.ApiPageResponse
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.request.MarkRelatedNotificationsReadRequest
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.response.NotificationPreferencesResponse
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.response.NotificationResponse
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.request.RegisterDeviceRequest
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.response.UnreadCountResponse
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.request.UpdateNotificationPreferencesRequest
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
    ): Response<ApiPageResponse<NotificationResponse>>

    @GET("api/v1/notifications/unread-count")
    suspend fun getUnreadCount(): Response<UnreadCountResponse>

    @POST("api/v1/notifications/{notificationId}/read")
    suspend fun markRead(
        @Path("notificationId") notificationId: String,
    ): Response<NotificationResponse>

    @POST("api/v1/notifications/read-all")
    suspend fun markAllRead(): Response<UnreadCountResponse>

    @POST("api/v1/notifications/read-related")
    suspend fun markRelatedRead(
        @Body request: MarkRelatedNotificationsReadRequest,
    ): Response<UnreadCountResponse>

    @GET("api/v1/notifications/preferences")
    suspend fun getPreferences(): Response<NotificationPreferencesResponse>

    @PATCH("api/v1/notifications/preferences")
    suspend fun updatePreferences(
        @Body request: UpdateNotificationPreferencesRequest,
    ): Response<NotificationPreferencesResponse>

    @POST("api/v1/devices/fcm-registration")
    suspend fun registerDevice(
        @Body request: RegisterDeviceRequest,
    ): Response<ResponseBody>
}
