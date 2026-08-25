package com.ahmetkaragunlu.guidemate.notification.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferenceUpdate
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationPreferencesViewModel
@Inject
constructor(
    private val repository: NotificationRepository,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val loadState = MutableStateFlow(ContentLoadState.LOADING)
    private val isUpdating = MutableStateFlow(false)
    private val userMessage = MutableStateFlow<String?>(null)
    private var refreshJob: Job? = null

    val uiState: StateFlow<NotificationPreferencesUiState> =
        combine(
            repository.preferences,
            loadState,
            isUpdating,
            userMessage,
        ) { preferences, currentLoadState, updating, message ->
            NotificationPreferencesUiState(
                preferences = preferences,
                loadState = currentLoadState,
                isUpdating = updating,
                userMessage = message,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NotificationPreferencesUiState(),
        )

    init {
        refresh()
    }

    fun refresh() {
        if (refreshJob?.isActive == true) return
        refreshJob =
            viewModelScope.launch {
                if (repository.preferences.value == null) {
                    loadState.value = ContentLoadState.LOADING
                }
                when (val result = repository.refreshPreferences()) {
                    is DataResult.Success -> loadState.value = ContentLoadState.CONTENT
                    is DataResult.Error -> {
                        userMessage.value = result.error.toMessage(resourceProvider)
                        loadState.value =
                            if (repository.preferences.value == null) {
                                ContentLoadState.ERROR
                            } else {
                                ContentLoadState.CONTENT
                            }
                    }
                }
            }
    }

    fun updateUpcomingTourReminders(enabled: Boolean) {
        update(NotificationPreferenceUpdate(upcomingTourRemindersEnabled = enabled))
    }

    fun updateChatMessages(enabled: Boolean) {
        update(NotificationPreferenceUpdate(chatMessagesEnabled = enabled))
    }

    fun updateReservationUpdates(enabled: Boolean) {
        update(NotificationPreferenceUpdate(reservationUpdatesEnabled = enabled))
    }

    fun updateReviewRequests(enabled: Boolean) {
        update(NotificationPreferenceUpdate(reviewRequestsEnabled = enabled))
    }

    fun updatePaymentsAndEarnings(enabled: Boolean) {
        update(NotificationPreferenceUpdate(paymentsAndEarningsEnabled = enabled))
    }

    fun updateNewReviews(enabled: Boolean) {
        update(NotificationPreferenceUpdate(newReviewsEnabled = enabled))
    }

    fun onMessageShown() {
        userMessage.value = null
    }

    private fun update(update: NotificationPreferenceUpdate) {
        if (isUpdating.value) return
        viewModelScope.launch {
            isUpdating.value = true
            when (val result = repository.updatePreferences(update)) {
                is DataResult.Success -> Unit
                is DataResult.Error -> {
                    userMessage.value = result.error.toMessage(resourceProvider)
                }
            }
            isUpdating.value = false
        }
    }
}
