package com.ahmetkaragunlu.guidemate.notification.data.push

import android.content.Context
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationSecurityEvent
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class NotificationPushTextResolverTest {
    private val context: Context = RuntimeEnvironment.getApplication()
    private val resolver = NotificationPushTextResolver(context)

    @Test
    fun `security event selects matching localized body with safe fallback`() {
        assertEquals(
            context.getString(R.string.notification_security_password_changed),
            resolver.body(target(NotificationSecurityEvent.PASSWORD_CHANGED)),
        )
        assertEquals(
            context.getString(R.string.notification_security_password_reset),
            resolver.body(target(NotificationSecurityEvent.PASSWORD_RESET)),
        )
        assertEquals(
            context.getString(R.string.notification_security_alert),
            resolver.body(target(NotificationSecurityEvent.UNKNOWN)),
        )
    }

    private fun target(securityEvent: NotificationSecurityEvent) =
        NotificationNavigationTarget(
            notificationId = "notification-1",
            type = NotificationType.SECURITY_ALERT,
            securityEvent = securityEvent,
        )
}
