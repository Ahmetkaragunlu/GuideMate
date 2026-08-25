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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationPreferencesViewModel
@Inject
constructor(
    private val repository: NotificationRepository,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val operationState = MutableStateFlow(NotificationPreferencesOperationState())
    private var refreshJob: Job? = null

    val uiState: StateFlow<NotificationPreferencesUiState> =
        combine(
            repository.preferences,
            operationState,
        ) { preferences, operation ->
            NotificationPreferencesUiState(
                preferences = preferences,
                loadState = operation.loadState,
                isUpdating = operation.isUpdating,
                userMessage = operation.userMessage,
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
                    operationState.update { it.copy(loadState = ContentLoadState.LOADING) }
                }
                when (val result = repository.refreshPreferences()) {
                    is DataResult.Success ->
                        operationState.update { it.copy(loadState = ContentLoadState.CONTENT) }
                    is DataResult.Error -> {
                        operationState.update {
                            it.copy(
                                loadState =
                                    if (repository.preferences.value == null) {
                                        ContentLoadState.ERROR
                                    } else {
                                        ContentLoadState.CONTENT
                                    },
                                userMessage = result.error.toMessage(resourceProvider),
                            )
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
        operationState.update { it.copy(userMessage = null) }
    }

    private fun update(update: NotificationPreferenceUpdate) {
        if (operationState.value.isUpdating) return
        viewModelScope.launch {
            operationState.update { it.copy(isUpdating = true) }
            when (val result = repository.updatePreferences(update)) {
                is DataResult.Success -> Unit
                is DataResult.Error -> {
                    operationState.update {
                        it.copy(userMessage = result.error.toMessage(resourceProvider))
                    }
                }
            }
            operationState.update { it.copy(isUpdating = false) }
        }
    }
}

private data class NotificationPreferencesOperationState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val isUpdating: Boolean = false,
    val userMessage: String? = null,
)
