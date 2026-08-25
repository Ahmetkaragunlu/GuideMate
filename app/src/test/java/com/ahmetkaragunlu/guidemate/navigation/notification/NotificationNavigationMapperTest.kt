package com.ahmetkaragunlu.guidemate.navigation.notification

import com.ahmetkaragunlu.guidemate.navigation.chat.ChatDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.GuideDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.tours.GuideTourDestination
import com.ahmetkaragunlu.guidemate.navigation.tourist.TouristDestination
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.TouristPaymentDestination
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationNavigationMapperTest {
    @Test
    fun `chat notification opens the same typed detail for both roles`() {
        val target = target(NotificationType.CHAT_MESSAGE, chatId = "chat-1")

        assertEquals(ChatDestination.Detail("chat-1"), target.toGuideDestination())
        assertEquals(ChatDestination.Detail("chat-1"), target.toTouristDestination())
    }

    @Test
    fun `guide tour notification requires both tour and session identifiers`() {
        val complete =
            target(
                type = NotificationType.TOUR_APPROVED,
                tourId = "tour-1",
                sessionId = "session-1",
            )
        val incomplete = target(type = NotificationType.TOUR_APPROVED, tourId = "tour-1")

        assertEquals(
            GuideTourDestination.Detail("tour-1", "session-1"),
            complete.toGuideDestination(),
        )
        assertEquals(GuideTourDestination.MyTours, incomplete.toGuideDestination())
    }

    @Test
    fun `tourist reservation and payment notifications use canonical identifiers`() {
        val reservation =
            target(
                type = NotificationType.RESERVATION_CONFIRMED,
                reservationId = "reservation-1",
            )
        val payment =
            target(type = NotificationType.PAYMENT_SUCCEEDED, paymentId = "payment-1")

        assertEquals(
            TouristDestination.ReservationDetail("reservation-1"),
            reservation.toTouristDestination(),
        )
        assertEquals(
            TouristPaymentDestination.Status(
                paymentId = "payment-1",
                openHostedIfRequired = false,
            ),
            payment.toTouristDestination(),
        )
    }

    @Test
    fun `missing or unknown targets fall back without crashing`() {
        assertEquals(
            GuideDestination.Home,
            target(NotificationType.CHAT_MESSAGE).toGuideDestination(),
        )
        assertEquals(
            TouristDestination.Home,
            target(NotificationType.UNKNOWN).toTouristDestination(),
        )
    }

    private fun target(
        type: NotificationType,
        chatId: String? = null,
        tourId: String? = null,
        sessionId: String? = null,
        reservationId: String? = null,
        paymentId: String? = null,
    ): NotificationNavigationTarget =
        NotificationNavigationTarget(
            notificationId = "notification-1",
            type = type,
            chatId = chatId,
            tourId = tourId,
            sessionId = sessionId,
            reservationId = reservationId,
            paymentId = paymentId,
        )
}
