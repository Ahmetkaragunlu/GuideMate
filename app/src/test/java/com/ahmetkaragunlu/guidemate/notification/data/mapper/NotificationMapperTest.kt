package com.ahmetkaragunlu.guidemate.notification.data.mapper

import com.ahmetkaragunlu.guidemate.notification.data.remote.model.NotificationResponseDto
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferenceUpdate
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationSecurityEvent
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NotificationMapperTest {
    @Test
    fun `payload maps typed identifiers and numeric values`() {
        val notification =
            NotificationResponseDto(
                    id = "notification-1",
                    type = "PAYMENT_SUCCEEDED",
                    actorId = 7,
                    actorDisplayName = "Ada",
                    payload =
                        mapOf(
                            "paymentId" to "payment-1",
                            "reservationId" to "reservation-1",
                            "rating" to "5",
                            "amountMinor" to 12_500.0,
                            "currencyCode" to "USD",
                        ),
                    isRead = false,
                    readAt = null,
                    createdAt = Instant.parse("2026-08-25T12:00:00Z"),
                )
                .toDomain()

        assertEquals(NotificationType.PAYMENT_SUCCEEDED, notification.type)
        assertEquals("Ada", notification.actorDisplayName)
        assertEquals("payment-1", notification.payload.paymentId)
        assertEquals("reservation-1", notification.payload.reservationId)
        assertEquals(5, notification.payload.rating)
        assertEquals(12_500L, notification.payload.amountMinor)
    }

    @Test
    fun `unknown type and blank payload values fall back safely`() {
        val notification =
            NotificationResponseDto(
                    id = "notification-2",
                    type = "NEW_BACKEND_TYPE",
                    actorId = null,
                    actorDisplayName = null,
                    payload = mapOf("chatId" to "  ", "paymentId" to null),
                    isRead = true,
                    readAt = Instant.parse("2026-08-25T12:01:00Z"),
                    createdAt = Instant.parse("2026-08-25T12:00:00Z"),
                )
                .toDomain()

        assertEquals(NotificationType.UNKNOWN, notification.type)
        assertNull(notification.payload.chatId)
        assertNull(notification.payload.paymentId)
    }

    @Test
    fun `security events map known and unknown values safely`() {
        assertEquals(
            NotificationSecurityEvent.PASSWORD_CHANGED,
            securityNotification("PASSWORD_CHANGED").payload.securityEvent,
        )
        assertEquals(
            NotificationSecurityEvent.PASSWORD_RESET,
            securityNotification("PASSWORD_RESET").payload.securityEvent,
        )
        assertEquals(
            NotificationSecurityEvent.UNKNOWN,
            securityNotification("FUTURE_SECURITY_EVENT").payload.securityEvent,
        )
    }

    @Test
    fun `preference update sends only explicitly changed fields`() {
        val dto =
            NotificationPreferenceUpdate(
                    chatMessagesEnabled = false,
                    newReviewsEnabled = true,
                )
                .toDto()

        assertEquals(false, dto.chatMessagesEnabled)
        assertEquals(true, dto.newReviewsEnabled)
        assertNull(dto.upcomingTourRemindersEnabled)
        assertNull(dto.reservationUpdatesEnabled)
    }

    private fun securityNotification(securityEvent: String) =
        NotificationResponseDto(
                id = "security-notification",
                type = "SECURITY_ALERT",
                actorId = null,
                actorDisplayName = null,
                payload = mapOf("securityEvent" to securityEvent),
                isRead = false,
                readAt = null,
                createdAt = Instant.parse("2026-09-01T12:00:00Z"),
            )
            .toDomain()
}
