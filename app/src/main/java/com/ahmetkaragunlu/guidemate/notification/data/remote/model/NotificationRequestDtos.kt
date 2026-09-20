package com.ahmetkaragunlu.guidemate.notification.data.remote.model

import com.google.gson.annotations.SerializedName

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

data class MarkRelatedNotificationsReadRequestDto(
    @SerializedName("targetType") val targetType: String,
    @SerializedName("targetId") val targetId: String,
)
