package com.ahmetkaragunlu.guidemate.notification.domain.model

enum class NotificationSecurityEvent {
    PASSWORD_CHANGED,
    PASSWORD_RESET,
    UNKNOWN,
    ;

    companion object {
        fun fromApiValue(value: String?): NotificationSecurityEvent =
            entries.firstOrNull { it.name == value?.trim()?.uppercase() } ?: UNKNOWN
    }
}
