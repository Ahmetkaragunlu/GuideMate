package com.ahmetkaragunlu.guidemate.notification.domain.push

import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetType
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationVisibleTarget
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotificationForegroundStateTest {
    private val state = NotificationForegroundState()

    @Test
    fun `same visible target suppresses system notification only while app is resumed`() {
        val notification = chatNotification("chat-1")
        state.setVisibleTarget(
            NotificationVisibleTarget(NotificationTargetType.CHAT, "chat-1"),
        )

        assertTrue(state.shouldDisplay(notification))

        state.setAppResumed(true)
        assertFalse(state.shouldDisplay(notification))

        state.setAppResumed(false)
        assertTrue(state.shouldDisplay(notification))
    }

    @Test
    fun `different visible target does not suppress system notification`() {
        state.setAppResumed(true)
        state.setVisibleTarget(
            NotificationVisibleTarget(NotificationTargetType.CHAT, "chat-2"),
        )

        assertTrue(state.shouldDisplay(chatNotification("chat-1")))
    }

    private fun chatNotification(chatId: String) =
        NotificationNavigationTarget(
            notificationId = "notification-1",
            type = NotificationType.CHAT_MESSAGE,
            chatId = chatId,
        )
}
