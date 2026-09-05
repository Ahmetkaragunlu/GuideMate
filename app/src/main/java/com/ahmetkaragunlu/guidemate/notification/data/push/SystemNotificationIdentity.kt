package com.ahmetkaragunlu.guidemate.notification.data.push

import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType

internal data class SystemNotificationIdentity(
    val tag: String?,
    val id: Int,
) {
    val requestCode: Int = 31 * id + tag.hashCode()
    val actionSuffix: String = tag ?: id.toString()
}

internal fun NotificationNavigationTarget.systemNotificationIdentity(): SystemNotificationIdentity =
    if (type == NotificationType.CHAT_MESSAGE && !chatId.isNullOrBlank()) {
        SystemNotificationIdentity(
            tag = "chat:$chatId",
            id = CHAT_NOTIFICATION_ID,
        )
    } else {
        SystemNotificationIdentity(
            tag = null,
            id = notificationId?.hashCode() ?: hashCode(),
        )
    }

private const val CHAT_NOTIFICATION_ID = 1
