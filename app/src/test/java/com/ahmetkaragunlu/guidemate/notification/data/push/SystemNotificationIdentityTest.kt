package com.ahmetkaragunlu.guidemate.notification.data.push

import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SystemNotificationIdentityTest {
    @Test
    fun `messages from same chat replace the same system notification`() {
        val first = chatNotification(notificationId = "notification-1", chatId = "chat-1")
        val second = chatNotification(notificationId = "notification-2", chatId = "chat-1")

        assertEquals(first.systemNotificationIdentity(), second.systemNotificationIdentity())
    }

    @Test
    fun `messages from different chats remain separate`() {
        val first = chatNotification(notificationId = "notification-1", chatId = "chat-1")
        val second = chatNotification(notificationId = "notification-2", chatId = "chat-2")

        assertNotEquals(first.systemNotificationIdentity(), second.systemNotificationIdentity())
    }

    @Test
    fun `non chat notifications keep their backend notification identity`() {
        val first = paymentNotification("notification-1")
        val second = paymentNotification("notification-2")

        assertNull(first.systemNotificationIdentity().tag)
        assertNotEquals(first.systemNotificationIdentity(), second.systemNotificationIdentity())
    }

    private fun chatNotification(
        notificationId: String,
        chatId: String,
    ) =
        NotificationNavigationTarget(
            notificationId = notificationId,
            type = NotificationType.CHAT_MESSAGE,
            chatId = chatId,
        )

    private fun paymentNotification(notificationId: String) =
        NotificationNavigationTarget(
            notificationId = notificationId,
            type = NotificationType.PAYMENT_SUCCEEDED,
            paymentId = "payment-1",
        )
}
