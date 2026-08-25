package com.ahmetkaragunlu.guidemate.notification.presentation.settings

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferences

data class NotificationPreferencesUiState(
    val preferences: NotificationPreferences? = null,
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val isUpdating: Boolean = false,
    val userMessage: String? = null,
)
