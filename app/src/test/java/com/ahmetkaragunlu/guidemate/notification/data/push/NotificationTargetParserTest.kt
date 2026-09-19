package com.ahmetkaragunlu.guidemate.notification.data.push

import android.content.Intent
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationSecurityEvent
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NotificationTargetParserTest {
    private val parser = NotificationTargetParser()

    @Test
    fun dataPayloadMapsEverySupportedTargetField() {
        val target =
            parser.fromData(
                mapOf(
                    "notificationId" to "notification-1",
                    "type" to "SECURITY_ALERT",
                    "chatId" to "chat-1",
                    "tourId" to "tour-1",
                    "sessionId" to "session-1",
                    "reservationId" to "reservation-1",
                    "paymentId" to "payment-1",
                    "securityEvent" to "PASSWORD_CHANGED",
                )
            )

        assertEquals(
            NotificationNavigationTarget(
                notificationId = "notification-1",
                type = NotificationType.SECURITY_ALERT,
                chatId = "chat-1",
                tourId = "tour-1",
                sessionId = "session-1",
                reservationId = "reservation-1",
                paymentId = "payment-1",
                securityEvent = NotificationSecurityEvent.PASSWORD_CHANGED,
            ),
            target,
        )
    }

    @Test
    fun intentTargetIsConsumedOnlyOnceAndExtrasAreCleared() {
        val target =
            NotificationNavigationTarget(
                notificationId = "notification-1",
                type = NotificationType.CHAT_MESSAGE,
                chatId = "chat-1",
            )
        val intent = parser.putExtras(Intent(), target)

        assertEquals(target, parser.consumeIntent(intent))
        assertNull(parser.consumeIntent(intent))
    }

    @Test
    fun missingPayloadIsIgnoredAndUnknownValuesUseSafeFallbacks() {
        assertNull(parser.fromData(emptyMap()))

        val target =
            parser.fromData(
                mapOf(
                    "notificationId" to "notification-1",
                    "type" to "NOT_A_REAL_TYPE",
                    "securityEvent" to "NOT_A_REAL_EVENT",
                )
            )

        assertEquals(NotificationType.UNKNOWN, target?.type)
        assertEquals(NotificationSecurityEvent.UNKNOWN, target?.securityEvent)
    }
}
