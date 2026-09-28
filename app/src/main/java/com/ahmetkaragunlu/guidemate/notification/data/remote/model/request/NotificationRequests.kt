package com.ahmetkaragunlu.guidemate.notification.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class UpdateNotificationPreferencesRequest(
    @SerializedName("upcomingTourRemindersEnabled") val upcomingTourRemindersEnabled: Boolean? = null,
    @SerializedName("chatMessagesEnabled") val chatMessagesEnabled: Boolean? = null,
    @SerializedName("reservationUpdatesEnabled") val reservationUpdatesEnabled: Boolean? = null,
    @SerializedName("reviewRequestsEnabled") val reviewRequestsEnabled: Boolean? = null,
    @SerializedName("paymentsAndEarningsEnabled") val paymentsAndEarningsEnabled: Boolean? = null,
    @SerializedName("newReviewsEnabled") val newReviewsEnabled: Boolean? = null,
)

data class RegisterDeviceRequest(
    @SerializedName("installationId") val installationId: String,
    @SerializedName("firebaseInstallationId") val firebaseInstallationId: String,
)

data class MarkRelatedNotificationsReadRequest(
    @SerializedName("targetType") val targetType: String,
    @SerializedName("targetId") val targetId: String,
)
