package com.ahmetkaragunlu.guidemate.screens.guide.notifications.model

data class GuideNotificationUiModel(
    val id: String,
    val type: GuideNotificationType,
    val occurredAtMillis: Long,
    val isRead: Boolean,
    val actorName: String = "",
    val tourTitle: String = "",
    val rating: Int? = null,
    val amount: Double? = null,
    val commentPreview: String? = null,
    val rejectionReason: String? = null,
)
