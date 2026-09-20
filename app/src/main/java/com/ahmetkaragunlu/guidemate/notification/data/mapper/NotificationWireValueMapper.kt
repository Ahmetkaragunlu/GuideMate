package com.ahmetkaragunlu.guidemate.notification.data.mapper

import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationSecurityEvent
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType

internal fun String?.toNotificationType(): NotificationType =
    NotificationType.entries.firstOrNull { it.name == this } ?: NotificationType.UNKNOWN

internal fun String?.toNotificationSecurityEvent(): NotificationSecurityEvent =
    NotificationSecurityEvent.entries.firstOrNull { it.name == this?.trim()?.uppercase() }
        ?: NotificationSecurityEvent.UNKNOWN
