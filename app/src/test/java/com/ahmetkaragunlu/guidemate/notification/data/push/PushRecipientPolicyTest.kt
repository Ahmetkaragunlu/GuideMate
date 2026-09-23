package com.ahmetkaragunlu.guidemate.notification.data.push

import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import com.ahmetkaragunlu.guidemate.testing.auth.authenticatedUser
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PushRecipientPolicyTest {
    @Test
    fun acceptsNotificationForActiveUser() {
        val activeUser = authenticatedUser()

        assertTrue(
            PushRecipientPolicy.targetsActiveUser(
                data = mapOf("recipientUserId" to "7"),
                userState = activeUser,
            ),
        )
    }

    @Test
    fun rejectsNotificationForDifferentUser() {
        val activeUser = authenticatedUser()

        assertFalse(
            PushRecipientPolicy.targetsActiveUser(
                data = mapOf("recipientUserId" to "84"),
                userState = activeUser,
            ),
        )
    }

    @Test
    fun rejectsNotificationWithoutAuthenticatedSession() {
        assertFalse(
            PushRecipientPolicy.targetsActiveUser(
                data = mapOf("recipientUserId" to "7"),
                userState = UserState(),
            ),
        )
    }

    @Test
    fun rejectsNotificationWithoutValidRecipientIdentity() {
        val activeUser = authenticatedUser()

        assertFalse(PushRecipientPolicy.targetsActiveUser(emptyMap(), activeUser))
        assertFalse(
            PushRecipientPolicy.targetsActiveUser(
                data = mapOf("recipientUserId" to "not-a-user-id"),
                userState = activeUser,
            ),
        )
    }
}
