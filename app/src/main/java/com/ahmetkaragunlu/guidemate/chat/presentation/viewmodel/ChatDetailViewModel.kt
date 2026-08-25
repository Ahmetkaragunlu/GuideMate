package com.ahmetkaragunlu.guidemate.chat.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.chat.domain.repository.ChatRepository
import com.ahmetkaragunlu.guidemate.chat.presentation.model.ChatDetailUiState
import com.ahmetkaragunlu.guidemate.chat.presentation.model.toMessageUiModel
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.navigation.chat.ChatDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val MAX_MESSAGE_LENGTH = 2_000

@HiltViewModel
class ChatDetailViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val chatId = savedStateHandle.toRoute<ChatDestination.Detail>().chatId
    private val inputText = MutableStateFlow("")
    private val requestState = MutableStateFlow(ChatDetailRequestState())
    private var loadJob: Job? = null

    val uiState: StateFlow<ChatDetailUiState> =
        combine(
            chatRepository.observeMessages(chatId),
            userRepository.userState,
            inputText,
            requestState,
        ) { history, userState, currentInput, currentRequestState ->
            val currentUserId = userState.userId
            ChatDetailUiState(
                messages =
                    currentUserId?.let { userId ->
                        history.messages.map { it.toMessageUiModel(userId) }
                    }.orEmpty(),
                inputText = currentInput,
                loadState = currentRequestState.loadState,
                errorMessage = currentRequestState.errorMessage,
                canLoadMore = history.hasMore,
                isLoadingMore = currentRequestState.isLoadingMore,
                olderLoadFailed = currentRequestState.olderLoadFailed,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChatDetailUiState(),
        )

    init {
        refresh()
        observeIncomingMessages()
    }

    fun refresh() {
        if (loadJob?.isActive == true) return
        loadJob =
            viewModelScope.launch {
                requestState.value = ChatDetailRequestState()
                requestState.value =
                    when (val result = chatRepository.loadInitialMessages(chatId)) {
                        is DataResult.Success ->
                            ChatDetailRequestState(loadState = ContentLoadState.CONTENT)
                        is DataResult.Error ->
                            ChatDetailRequestState(
                                loadState = ContentLoadState.ERROR,
                                errorMessage = result.error.toMessage(resourceProvider),
                            )
                    }
            }
    }

    fun loadOlderMessages() {
        val state = requestState.value
        if (state.isLoadingMore || !uiState.value.canLoadMore) return
        viewModelScope.launch {
            requestState.value = state.copy(isLoadingMore = true, olderLoadFailed = false)
            requestState.value =
                when (chatRepository.loadOlderMessages(chatId)) {
                    is DataResult.Success -> requestState.value.copy(isLoadingMore = false)
                    is DataResult.Error ->
                        requestState.value.copy(
                            isLoadingMore = false,
                            olderLoadFailed = true,
                        )
                }
        }
    }

    fun onTextChange(text: String) {
        if (text.length <= MAX_MESSAGE_LENGTH) inputText.value = text
    }

    fun sendMessage() {
        val message = inputText.value.trim()
        if (message.isEmpty()) return
        inputText.value = ""
        viewModelScope.launch { chatRepository.sendMessage(chatId, message) }
    }

    fun retryMessage(clientMessageId: String) {
        viewModelScope.launch { chatRepository.retryMessage(chatId, clientMessageId) }
    }

    private fun observeIncomingMessages() {
        viewModelScope.launch {
            chatRepository.observeMessages(chatId)
                .map { it.messages.lastOrNull()?.messageId }
                .distinctUntilChanged()
                .collect { messageId ->
                    if (messageId != null) chatRepository.markRead(chatId)
                }
        }
    }

    private data class ChatDetailRequestState(
        val loadState: ContentLoadState = ContentLoadState.LOADING,
        val errorMessage: String? = null,
        val isLoadingMore: Boolean = false,
        val olderLoadFailed: Boolean = false,
    )
}
