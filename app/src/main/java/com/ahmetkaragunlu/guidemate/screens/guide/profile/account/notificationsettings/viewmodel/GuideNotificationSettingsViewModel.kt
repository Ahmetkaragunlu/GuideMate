package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.notificationsettings.viewmodel

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.notificationsettings.model.GuideNotificationSettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class GuideNotificationSettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(GuideNotificationSettingsUiState())
    val uiState: StateFlow<GuideNotificationSettingsUiState> = _uiState.asStateFlow()

    fun onUpcomingTourRemindersChanged(enabled: Boolean) {
        _uiState.update { it.copy(upcomingTourReminders = enabled) }
    }

    fun onTouristMessagesChanged(enabled: Boolean) {
        _uiState.update { it.copy(touristMessages = enabled) }
    }

    fun onReservationUpdatesChanged(enabled: Boolean) {
        _uiState.update { it.copy(reservationUpdates = enabled) }
    }

    fun onPaymentsAndEarningsChanged(enabled: Boolean) {
        _uiState.update { it.copy(paymentsAndEarnings = enabled) }
    }

    fun onNewReviewsChanged(enabled: Boolean) {
        _uiState.update { it.copy(newReviews = enabled) }
    }

    fun onSecurityAlertsChanged(enabled: Boolean) {
        _uiState.update { it.copy(securityAlerts = enabled) }
    }
}
