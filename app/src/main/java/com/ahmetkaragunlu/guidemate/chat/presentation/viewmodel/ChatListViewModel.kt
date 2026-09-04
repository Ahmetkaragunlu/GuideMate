package com.ahmetkaragunlu.guidemate.chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.chat.domain.repository.ChatRepository
import com.ahmetkaragunlu.guidemate.chat.presentation.model.ChatListUiState
import com.ahmetkaragunlu.guidemate.chat.presentation.mapper.toChatUiModel
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetReference
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetType
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
    private val notificationRepository: NotificationRepository,
    userRepository: UserRepository,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val operationState = MutableStateFlow(ChatListOperationState())
    private var refreshJob: Job? = null
    private var clearJob: Job? = null

    val uiState: StateFlow<ChatListUiState> =
        combine(
            chatRepository.conversations,
            chatRepository.totalUnreadCount,
            userRepository.userState,
            operationState,
        ) { conversations, unreadCount, userState, operation ->
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
                loadState = operation.loadState,
                errorMessage = operation.errorMessage,
                userMessage = operation.userMessage,
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
                    operationState.value =
                        operationState.value.copy(loadState = ContentLoadState.LOADING)
                }
                operationState.value = operationState.value.copy(errorMessage = null)
                when (val result = chatRepository.refreshConversations()) {
                    is DataResult.Success -> {
                        chatRepository.refreshUnreadCount()
                        operationState.value =
                            operationState.value.copy(loadState = ContentLoadState.CONTENT)
                    }
                    is DataResult.Error -> {
                        operationState.value =
                            operationState.value.copy(
                                loadState = ContentLoadState.ERROR,
                                errorMessage = result.error.toMessage(resourceProvider),
                            )
                    }
                }
            }
    }

    fun clearConversation(chatId: String) {
        if (clearJob?.isActive == true) return
        clearJob =
            viewModelScope.launch {
                when (val result = chatRepository.clearConversation(chatId)) {
                    is DataResult.Success -> {
                        notificationRepository.markRelatedRead(
                            NotificationTargetReference(NotificationTargetType.CHAT, chatId),
                        )
                    }
                    is DataResult.Error -> {
                        operationState.value =
                            operationState.value.copy(
                                userMessage = result.error.toMessage(resourceProvider),
                            )
                    }
                }
            }
    }

    fun onMessageShown() {
        operationState.value = operationState.value.copy(userMessage = null)
    }
}

private data class ChatListOperationState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val errorMessage: String? = null,
    val userMessage: String? = null,
)
