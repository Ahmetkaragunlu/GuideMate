package com.ahmetkaragunlu.guidemate.notification.domain.navigation

import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NotificationNavigationCoordinatorTest {
    private val coordinator = NotificationNavigationCoordinator()

    @Test
    fun `matching target is consumed`() {
        val target = target("chat-1")

        coordinator.offer(target)
        coordinator.consume(target)

        assertNull(coordinator.pendingTarget.value)
    }

    @Test
    fun `different target does not consume pending navigation`() {
        val pending = target("chat-1")

        coordinator.offer(pending)
        coordinator.consume(target("chat-2"))

        assertEquals(pending, coordinator.pendingTarget.value)
    }

    @Test
    fun `new target replaces older pending navigation`() {
        val latest = target("chat-2")

        coordinator.offer(target("chat-1"))
        coordinator.offer(latest)

        assertEquals(latest, coordinator.pendingTarget.value)
    }

    private fun target(chatId: String): NotificationNavigationTarget =
        NotificationNavigationTarget(
            notificationId = "notification-$chatId",
            type = NotificationType.CHAT_MESSAGE,
            chatId = chatId,
        )
}
