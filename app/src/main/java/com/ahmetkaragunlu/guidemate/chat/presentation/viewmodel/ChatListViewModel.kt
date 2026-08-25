package com.ahmetkaragunlu.guidemate.chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.chat.domain.repository.ChatRepository
import com.ahmetkaragunlu.guidemate.chat.presentation.model.ChatListUiState
import com.ahmetkaragunlu.guidemate.chat.presentation.model.toChatUiModel
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
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
class ChatListViewModel
@Inject
constructor(
    private val chatRepository: ChatRepository,
    notificationRepository: NotificationRepository,
    userRepository: UserRepository,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val loadState = MutableStateFlow(ContentLoadState.LOADING)
    private val errorMessage = MutableStateFlow<String?>(null)
    private var refreshJob: Job? = null

    val uiState: StateFlow<ChatListUiState> =
        combine(
            chatRepository.conversations,
            chatRepository.totalUnreadCount,
            userRepository.userState,
            loadState,
            errorMessage,
        ) { conversations, unreadCount, userState, currentLoadState, currentError ->
            val currentUserId = userState.userId
            val chats =
                if (currentUserId == null) {
                    emptyList()
                } else {
                    conversations.mapNotNull { it.toChatUiModel(currentUserId) }
                }
            ChatListUiState(
                chats = chats,
                totalUnreadCount = unreadCount,
                loadState = currentLoadState,
                errorMessage = currentError,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChatListUiState(),
        )

    init {
        refresh()
        viewModelScope.launch {
            notificationRepository.pushEvents.collect { target ->
                if (target.type == NotificationType.CHAT_MESSAGE) refresh()
            }
        }
    }

    fun refresh() {
        if (refreshJob?.isActive == true) return
        refreshJob =
            viewModelScope.launch {
                if (chatRepository.conversations.value.isEmpty()) {
                    loadState.value = ContentLoadState.LOADING
                }
                errorMessage.value = null
                when (val result = chatRepository.refreshConversations()) {
                    is DataResult.Success -> {
                        chatRepository.refreshUnreadCount()
                        loadState.value = ContentLoadState.CONTENT
                    }
                    is DataResult.Error -> {
                        errorMessage.value = result.error.toMessage(resourceProvider)
                        loadState.value = ContentLoadState.ERROR
                    }
                }
            }
    }
}
