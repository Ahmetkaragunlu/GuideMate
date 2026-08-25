package com.ahmetkaragunlu.guidemate.notification.data.push

import android.content.Intent
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationTargetParser @Inject constructor() {
    fun fromData(data: Map<String, String>): NotificationNavigationTarget? =
        createTarget(
            notificationId = data[KEY_NOTIFICATION_ID],
            type = data[KEY_TYPE],
            chatId = data[KEY_CHAT_ID],
            tourId = data[KEY_TOUR_ID],
            sessionId = data[KEY_SESSION_ID],
            reservationId = data[KEY_RESERVATION_ID],
            paymentId = data[KEY_PAYMENT_ID],
        )

    fun consumeIntent(intent: Intent?): NotificationNavigationTarget? {
        if (intent?.getBooleanExtra(KEY_NOTIFICATION_INTENT, false) != true) return null
        val target =
            createTarget(
                notificationId = intent.getStringExtra(KEY_NOTIFICATION_ID),
                type = intent.getStringExtra(KEY_TYPE),
                chatId = intent.getStringExtra(KEY_CHAT_ID),
                tourId = intent.getStringExtra(KEY_TOUR_ID),
                sessionId = intent.getStringExtra(KEY_SESSION_ID),
                reservationId = intent.getStringExtra(KEY_RESERVATION_ID),
                paymentId = intent.getStringExtra(KEY_PAYMENT_ID),
            )
        clearNotificationExtras(intent)
        return target
    }

    fun putExtras(
        intent: Intent,
        target: NotificationNavigationTarget,
    ): Intent =
        intent.apply {
            putExtra(KEY_NOTIFICATION_INTENT, true)
            putExtra(KEY_NOTIFICATION_ID, target.notificationId)
            putExtra(KEY_TYPE, target.type.name)
            putExtra(KEY_CHAT_ID, target.chatId)
            putExtra(KEY_TOUR_ID, target.tourId)
            putExtra(KEY_SESSION_ID, target.sessionId)
            putExtra(KEY_RESERVATION_ID, target.reservationId)
            putExtra(KEY_PAYMENT_ID, target.paymentId)
        }

    private fun createTarget(
        notificationId: String?,
        type: String?,
        chatId: String?,
        tourId: String?,
        sessionId: String?,
        reservationId: String?,
        paymentId: String?,
    ): NotificationNavigationTarget? {
        if (notificationId.isNullOrBlank() && type.isNullOrBlank()) return null
        return NotificationNavigationTarget(
            notificationId = notificationId,
            type = NotificationType.fromApiValue(type),
            chatId = chatId,
            tourId = tourId,
            sessionId = sessionId,
            reservationId = reservationId,
            paymentId = paymentId,
        )
    }

    private fun clearNotificationExtras(intent: Intent) {
        NOTIFICATION_EXTRA_KEYS.forEach(intent::removeExtra)
    }

    private companion object {
        const val KEY_NOTIFICATION_INTENT = "guidemate.notification.intent"
        const val KEY_NOTIFICATION_ID = "notificationId"
        const val KEY_TYPE = "type"
        const val KEY_CHAT_ID = "chatId"
        const val KEY_TOUR_ID = "tourId"
        const val KEY_SESSION_ID = "sessionId"
        const val KEY_RESERVATION_ID = "reservationId"
        const val KEY_PAYMENT_ID = "paymentId"

        val NOTIFICATION_EXTRA_KEYS =
            listOf(
                KEY_NOTIFICATION_INTENT,
                KEY_NOTIFICATION_ID,
                KEY_TYPE,
                KEY_CHAT_ID,
                KEY_TOUR_ID,
                KEY_SESSION_ID,
                KEY_RESERVATION_ID,
                KEY_PAYMENT_ID,
            )
    }
}
