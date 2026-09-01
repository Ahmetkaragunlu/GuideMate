package com.ahmetkaragunlu.guidemate.notification.data.mapper

import com.ahmetkaragunlu.guidemate.notification.data.remote.model.NotificationPreferencesResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.NotificationResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.UpdateNotificationPreferencesRequestDto
import com.ahmetkaragunlu.guidemate.notification.domain.model.AppNotification
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPayload
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferenceUpdate
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferences
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationSecurityEvent
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType

internal fun NotificationResponseDto.toDomain(): AppNotification =
    AppNotification(
        notificationId = id,
        type = NotificationType.fromApiValue(type),
        actorDisplayName = actorDisplayName,
        payload = payload.toNotificationPayload(),
        isRead = isRead,
        createdAt = createdAt,
    )

internal fun NotificationPreferencesResponseDto.toDomain(): NotificationPreferences =
    NotificationPreferences(
        upcomingTourRemindersEnabled = upcomingTourRemindersEnabled,
        chatMessagesEnabled = chatMessagesEnabled,
        reservationUpdatesEnabled = reservationUpdatesEnabled,
        reviewRequestsEnabled = reviewRequestsEnabled,
        paymentsAndEarningsEnabled = paymentsAndEarningsEnabled,
        newReviewsEnabled = newReviewsEnabled,
        securityAlertsEnabled = securityAlertsEnabled,
    )

internal fun NotificationPreferenceUpdate.toDto(): UpdateNotificationPreferencesRequestDto =
    UpdateNotificationPreferencesRequestDto(
        upcomingTourRemindersEnabled = upcomingTourRemindersEnabled,
        chatMessagesEnabled = chatMessagesEnabled,
        reservationUpdatesEnabled = reservationUpdatesEnabled,
        reviewRequestsEnabled = reviewRequestsEnabled,
        paymentsAndEarningsEnabled = paymentsAndEarningsEnabled,
        newReviewsEnabled = newReviewsEnabled,
    )

private fun Map<String, Any?>?.toNotificationPayload(): NotificationPayload =
    NotificationPayload(
        chatId = stringValue("chatId"),
        tourId = stringValue("tourId"),
        sessionId = stringValue("sessionId"),
        reservationId = stringValue("reservationId"),
        paymentId = stringValue("paymentId"),
        reviewId = stringValue("reviewId"),
        withdrawalId = stringValue("withdrawalId"),
        tourTitle = stringValue("tourTitle"),
        commentPreview = stringValue("commentPreview"),
        rejectionReason = stringValue("rejectionReason"),
        messagePreview = stringValue("messagePreview"),
        rating = intValue("rating"),
        amountMinor = longValue("amountMinor"),
        currencyCode = stringValue("currencyCode"),
        securityEvent = NotificationSecurityEvent.fromApiValue(stringValue("securityEvent")),
    )

private fun Map<String, Any?>?.stringValue(key: String): String? =
    this?.get(key)?.toString()?.trim()?.takeIf { it.isNotEmpty() && it != "null" }

private fun Map<String, Any?>?.longValue(key: String): Long? =
    when (val value = this?.get(key)) {
        is Number -> value.toLong()
        is String -> value.toLongOrNull()
        else -> null
    }

private fun Map<String, Any?>?.intValue(key: String): Int? =
    when (val value = this?.get(key)) {
        is Number -> value.toInt()
        is String -> value.toIntOrNull()
        else -> null
    }
