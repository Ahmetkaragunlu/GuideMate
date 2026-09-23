package com.ahmetkaragunlu.guidemate.notification.data.push

import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState

internal object PushRecipientPolicy {
    private const val RECIPIENT_USER_ID = "recipientUserId"

    fun targetsActiveUser(
        data: Map<String, String>,
        userState: UserState,
    ): Boolean =
        userState.isAuthenticated && data[RECIPIENT_USER_ID]?.toLongOrNull() == userState.userId
}
