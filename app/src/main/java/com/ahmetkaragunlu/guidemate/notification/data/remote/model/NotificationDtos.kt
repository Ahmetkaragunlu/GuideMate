package com.ahmetkaragunlu.guidemate.notification.data.remote.model

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class NotificationResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String?,
    @SerializedName("actorId") val actorId: Long?,
    @SerializedName("actorDisplayName") val actorDisplayName: String?,
    @SerializedName("payload") val payload: Map<String, Any?>?,
    @SerializedName("read") val isRead: Boolean,
    @SerializedName("readAt") val readAt: Instant?,
    @SerializedName("createdAt") val createdAt: Instant,
)

data class NotificationPreferencesResponseDto(
    @SerializedName("upcomingTourRemindersEnabled") val upcomingTourRemindersEnabled: Boolean,
    @SerializedName("chatMessagesEnabled") val chatMessagesEnabled: Boolean,
    @SerializedName("reservationUpdatesEnabled") val reservationUpdatesEnabled: Boolean,
    @SerializedName("reviewRequestsEnabled") val reviewRequestsEnabled: Boolean,
    @SerializedName("paymentsAndEarningsEnabled") val paymentsAndEarningsEnabled: Boolean,
    @SerializedName("newReviewsEnabled") val newReviewsEnabled: Boolean,
    @SerializedName("securityAlertsEnabled") val securityAlertsEnabled: Boolean,
)

data class UpdateNotificationPreferencesRequestDto(
    @SerializedName("upcomingTourRemindersEnabled") val upcomingTourRemindersEnabled: Boolean? = null,
    @SerializedName("chatMessagesEnabled") val chatMessagesEnabled: Boolean? = null,
    @SerializedName("reservationUpdatesEnabled") val reservationUpdatesEnabled: Boolean? = null,
    @SerializedName("reviewRequestsEnabled") val reviewRequestsEnabled: Boolean? = null,
    @SerializedName("paymentsAndEarningsEnabled") val paymentsAndEarningsEnabled: Boolean? = null,
    @SerializedName("newReviewsEnabled") val newReviewsEnabled: Boolean? = null,
)

data class RegisterDeviceRequestDto(
    @SerializedName("installationId") val installationId: String,
    @SerializedName("firebaseInstallationId") val firebaseInstallationId: String,
)

data class UnreadCountResponseDto(
    @SerializedName("unreadCount") val unreadCount: Long,
)

data class MarkRelatedNotificationsReadRequestDto(
    @SerializedName("targetType") val targetType: String,
    @SerializedName("targetId") val targetId: String,
)
