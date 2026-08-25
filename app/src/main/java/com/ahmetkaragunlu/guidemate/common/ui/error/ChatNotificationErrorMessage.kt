package com.ahmetkaragunlu.guidemate.common.ui.error

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider

internal fun BackendErrorCode.chatNotificationErrorMessage(resourceProvider: ResourceProvider): String? =
    when (this) {
        BackendErrorCode.NOTIFICATION_NOT_FOUND ->
            resourceProvider.getString(R.string.error_notification_not_found)
        BackendErrorCode.CHAT_NOT_FOUND -> resourceProvider.getString(R.string.error_chat_not_found)
        BackendErrorCode.CHAT_PARTICIPANT_INVALID ->
            resourceProvider.getString(R.string.error_chat_participant_invalid)
        BackendErrorCode.CHAT_MESSAGE_NOT_FOUND ->
            resourceProvider.getString(R.string.error_chat_message_not_found)
        BackendErrorCode.CHAT_MESSAGE_TOO_LONG ->
            resourceProvider.getString(R.string.error_chat_message_too_long)
        else -> null
    }
