package com.ahmetkaragunlu.guidemate.notification.domain.push

import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetReference

interface SystemNotificationController {
    fun createChannel()

    fun show(target: NotificationNavigationTarget)

    fun dismiss(target: NotificationNavigationTarget)

    fun dismissRelated(target: NotificationTargetReference)

    fun dismissAll()
}
