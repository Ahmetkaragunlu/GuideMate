package com.ahmetkaragunlu.guidemate.notification.data.repository

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.notification.data.realtime.NotificationRealtimeClient
import com.ahmetkaragunlu.guidemate.notification.data.remote.api.NotificationApi
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.MarkRelatedNotificationsReadRequestDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.NotificationPreferencesResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.NotificationResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.RegisterDeviceRequestDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.UnreadCountResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.UpdateNotificationPreferencesRequestDto
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetReference
import com.ahmetkaragunlu.guidemate.notification.domain.push.SystemNotificationController
import java.time.Instant
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

internal class FakeSystemNotificationController : SystemNotificationController {
    val dismissedTargets = mutableListOf<NotificationNavigationTarget>()
    val dismissedReferences = mutableListOf<NotificationTargetReference>()
    var dismissAllCalls = 0

    override fun createChannel() = Unit

    override fun show(target: NotificationNavigationTarget) = Unit

    override fun dismiss(target: NotificationNavigationTarget) {
        dismissedTargets += target
    }

    override fun dismissRelated(target: NotificationTargetReference) {
        dismissedReferences += target
    }

    override fun dismissAll() {
        dismissAllCalls++
    }
}

internal class FakeNotificationRealtimeClient : NotificationRealtimeClient {
    private val mutableEvents = MutableSharedFlow<Unit>(replay = 1)
    override val events: Flow<Unit> = mutableEvents
    var connectCalls = 0

    override fun connect() {
        connectCalls++
    }

    override fun disconnect() = Unit

    fun emit() {
        mutableEvents.tryEmit(Unit)
    }
}

internal class FakeNotificationApi(
    private val unreadCount: Long = 0,
) : NotificationApi {
    val requestedPages = mutableListOf<Int>()
    var deviceRequest: RegisterDeviceRequestDto? = null
    var relatedReadRequest: MarkRelatedNotificationsReadRequestDto? = null
    var notificationRequestStarted: CompletableDeferred<Unit>? = null
    var notificationResponseGate: CompletableDeferred<Unit>? = null
    var markReadCalls = 0
    var firstPageNotificationId = "notification-1"

    override suspend fun getNotifications(
        page: Int,
        size: Int,
    ): Response<ApiPageResponse<NotificationResponseDto>> {
        requestedPages += page
        val notificationId = firstPageNotificationId
        notificationRequestStarted?.complete(Unit)
        notificationResponseGate?.await()
        val content =
            when (page) {
                0 -> listOf(notification(notificationId, minute = 1, isRead = false))
                else ->
                    listOf(
                        notification("notification-1", minute = 1, isRead = false),
                        notification("notification-2", minute = 2, isRead = false),
                    )
            }
        return Response.success(
            ApiPageResponse(
                content = content,
                page = page,
                size = size,
                totalElements = 2,
                totalPages = 2,
                isFirst = page == 0,
                isLast = page > 0,
            ),
        )
    }

    override suspend fun getUnreadCount(): Response<UnreadCountResponseDto> =
        Response.success(UnreadCountResponseDto(unreadCount))

    override suspend fun markRead(
        notificationId: String,
    ): Response<NotificationResponseDto> {
        markReadCalls++
        return Response.success(notification(notificationId, minute = 1, isRead = true))
    }

    override suspend fun markAllRead(): Response<UnreadCountResponseDto> =
        Response.success(UnreadCountResponseDto(0))

    override suspend fun markRelatedRead(
        request: MarkRelatedNotificationsReadRequestDto,
    ): Response<UnreadCountResponseDto> {
        relatedReadRequest = request
        return Response.success(UnreadCountResponseDto(unreadCount))
    }

    override suspend fun getPreferences(): Response<NotificationPreferencesResponseDto> =
        Response.success(preferences())

    override suspend fun updatePreferences(
        request: UpdateNotificationPreferencesRequestDto,
    ): Response<NotificationPreferencesResponseDto> = Response.success(preferences())

    override suspend fun registerDevice(
        request: RegisterDeviceRequestDto,
    ): Response<ResponseBody> {
        deviceRequest = request
        return Response.success("".toResponseBody())
    }

    private fun notification(
        id: String,
        minute: Int,
        isRead: Boolean,
    ): NotificationResponseDto =
        NotificationResponseDto(
            id = id,
            type = "CHAT_MESSAGE",
            actorId = 9,
            actorDisplayName = "Guide",
            payload = mapOf("chatId" to "chat-1"),
            isRead = isRead,
            readAt = if (isRead) Instant.parse("2026-08-25T12:03:00Z") else null,
            createdAt = Instant.parse("2026-08-25T12:0${minute}:00Z"),
        )

    private fun preferences(): NotificationPreferencesResponseDto =
        NotificationPreferencesResponseDto(
            upcomingTourRemindersEnabled = true,
            chatMessagesEnabled = true,
            reservationUpdatesEnabled = true,
            reviewRequestsEnabled = true,
            paymentsAndEarningsEnabled = true,
            newReviewsEnabled = true,
            securityAlertsEnabled = true,
        )
}
