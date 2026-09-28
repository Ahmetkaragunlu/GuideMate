package com.ahmetkaragunlu.guidemate.notification.data.remote.model.response

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class NotificationResponse(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String?,
    @SerializedName("actorId") val actorId: Long?,
    @SerializedName("actorDisplayName") val actorDisplayName: String?,
    @SerializedName("payload") val payload: Map<String, Any?>?,
    @SerializedName("read") val isRead: Boolean,
    @SerializedName("readAt") val readAt: Instant?,
    @SerializedName("createdAt") val createdAt: Instant,
)

data class NotificationPreferencesResponse(
    @SerializedName("upcomingTourRemindersEnabled") val upcomingTourRemindersEnabled: Boolean,
    @SerializedName("chatMessagesEnabled") val chatMessagesEnabled: Boolean,
    @SerializedName("reservationUpdatesEnabled") val reservationUpdatesEnabled: Boolean,
    @SerializedName("reviewRequestsEnabled") val reviewRequestsEnabled: Boolean,
    @SerializedName("paymentsAndEarningsEnabled") val paymentsAndEarningsEnabled: Boolean,
    @SerializedName("newReviewsEnabled") val newReviewsEnabled: Boolean,
    @SerializedName("securityAlertsEnabled") val securityAlertsEnabled: Boolean,
)

data class UnreadCountResponse(
    @SerializedName("unreadCount") val unreadCount: Long,
)
