package com.ahmetkaragunlu.guidemate.notification.presentation.mapper

import com.ahmetkaragunlu.guidemate.notification.domain.model.AppNotification
import com.ahmetkaragunlu.guidemate.notification.presentation.model.NotificationUiModel

internal fun AppNotification.toUiModel(): NotificationUiModel =
    NotificationUiModel(
        id = notificationId,
        type = type,
        occurredAtMillis = createdAt.toEpochMilli(),
        isRead = isRead,
        navigationTarget = navigationTarget,
        actorName = actorDisplayName.orEmpty(),
        tourTitle = payload.tourTitle.orEmpty(),
        rating = payload.rating,
        amountMinor = payload.amountMinor,
        commentPreview = payload.commentPreview,
        messagePreview = payload.messagePreview,
        rejectionReason = payload.rejectionReason,
        securityEvent = payload.securityEvent,
    )
