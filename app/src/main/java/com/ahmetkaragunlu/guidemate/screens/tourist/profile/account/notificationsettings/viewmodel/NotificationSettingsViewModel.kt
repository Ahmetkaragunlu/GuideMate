package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.notificationsettings.viewmodel

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.notificationsettings.model.NotificationSettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationSettingsUiState())
    val uiState: StateFlow<NotificationSettingsUiState> = _uiState.asStateFlow()

    fun onUpcomingReminderChanged(enabled: Boolean) {
        _uiState.update { it.copy(upcomingReminder = enabled) }
    }

    fun onGuideMessagesChanged(enabled: Boolean) {
        _uiState.update { it.copy(guideMessages = enabled) }
    }

    fun onReservationUpdatesChanged(enabled: Boolean) {
        _uiState.update { it.copy(reservationUpdates = enabled) }
    }

    fun onReviewRequestsChanged(enabled: Boolean) {
        _uiState.update { it.copy(reviewRequests = enabled) }
    }
}
