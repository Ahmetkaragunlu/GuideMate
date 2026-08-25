package com.ahmetkaragunlu.guidemate.notification.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import com.ahmetkaragunlu.guidemate.notification.presentation.mapper.toUiModel
import com.ahmetkaragunlu.guidemate.notification.presentation.model.NotificationUiState
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
class NotificationViewModel
@Inject
constructor(
    private val repository: NotificationRepository,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val loadState = MutableStateFlow(ContentLoadState.LOADING)
    private val isLoadingMore = MutableStateFlow(false)
    private val isMarkingAllRead = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)
    private var refreshJob: Job? = null

    val uiState: StateFlow<NotificationUiState> =
        combine(
            repository.notifications,
            repository.unreadCount,
            repository.hasMoreNotifications,
            loadState,
            combine(isLoadingMore, isMarkingAllRead, errorMessage) { more, marking, error ->
                Triple(more, marking, error)
            },
        ) { notifications, unreadCount, hasMore, currentLoadState, operationState ->
            NotificationUiState(
                notifications = notifications.map { it.toUiModel() },
                unreadCount = unreadCount,
                hasMore = hasMore,
                loadState = currentLoadState,
                isLoadingMore = operationState.first,
                isMarkingAllRead = operationState.second,
                errorMessage = operationState.third,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NotificationUiState(),
        )

    init {
        refresh()
    }

    fun refresh() {
        if (refreshJob?.isActive == true) return
        refreshJob =
            viewModelScope.launch {
                if (repository.notifications.value.isEmpty()) {
                    loadState.value = ContentLoadState.LOADING
                }
                errorMessage.value = null
                val notificationsResult = repository.refreshNotifications()
                val unreadCountResult = repository.refreshUnreadCount()
                when (notificationsResult) {
                    is DataResult.Success -> {
                        loadState.value = ContentLoadState.CONTENT
                    }
                    is DataResult.Error -> {
                        errorMessage.value = notificationsResult.error.toMessage(resourceProvider)
                        loadState.value =
                            if (repository.notifications.value.isEmpty()) {
                                ContentLoadState.ERROR
                            } else {
                                ContentLoadState.CONTENT
                        }
                    }
                }
                if (notificationsResult is DataResult.Success && unreadCountResult is DataResult.Error) {
                    errorMessage.value = unreadCountResult.error.toMessage(resourceProvider)
                }
            }
    }

    fun loadMore() {
        if (isLoadingMore.value || !repository.hasMoreNotifications.value) return
        viewModelScope.launch {
            isLoadingMore.value = true
            val result = repository.loadMoreNotifications()
            if (result is DataResult.Error) {
                errorMessage.value = result.error.toMessage(resourceProvider)
            }
            isLoadingMore.value = false
        }
    }

    fun markRead(notificationId: String) {
        val notification = repository.notifications.value.firstOrNull {
            it.notificationId == notificationId
        }
        if (notification?.isRead == true) return
        viewModelScope.launch {
            val result = repository.markRead(notificationId)
            when (result) {
                is DataResult.Success -> repository.refreshUnreadCount()
                is DataResult.Error -> {
                    errorMessage.value = result.error.toMessage(resourceProvider)
                }
            }
        }
    }

    fun markAllRead() {
        if (isMarkingAllRead.value || repository.unreadCount.value == 0) return
        viewModelScope.launch {
            isMarkingAllRead.value = true
            val result = repository.markAllRead()
            if (result is DataResult.Error) {
                errorMessage.value = result.error.toMessage(resourceProvider)
            }
            isMarkingAllRead.value = false
        }
    }

    fun onMessageShown() {
        errorMessage.value = null
    }
}
