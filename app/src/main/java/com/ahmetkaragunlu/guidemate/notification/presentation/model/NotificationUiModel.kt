package com.ahmetkaragunlu.guidemate.notification.presentation.model

import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationSecurityEvent
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType

data class NotificationUiModel(
    val id: String,
    val type: NotificationType,
    val occurredAtMillis: Long,
    val isRead: Boolean,
    val navigationTarget: NotificationNavigationTarget,
    val actorName: String = "",
    val tourTitle: String = "",
    val rating: Int? = null,
    val amountMinor: Long? = null,
    val commentPreview: String? = null,
    val messagePreview: String? = null,
    val rejectionReason: String? = null,
    val securityEvent: NotificationSecurityEvent = NotificationSecurityEvent.UNKNOWN,
)
