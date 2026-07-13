package com.ahmetkaragunlu.guidemate.screens.common.chat.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.navigation.chat.CHAT_ID_ARGUMENT
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatDetailUiState
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.toMessageUiModel
import com.ahmetkaragunlu.guidemate.screens.common.chat.store.ChatStore
import com.ahmetkaragunlu.guidemate.screens.common.chat.store.mockCurrentUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

private const val MAX_MESSAGE_LENGTH = 2_000

@HiltViewModel
class ChatDetailViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val chatStore: ChatStore,
) : ViewModel() {
    private val chatId: String = checkNotNull(savedStateHandle[CHAT_ID_ARGUMENT])
    private val viewerRole = MutableStateFlow<UserRole?>(null)
    private val inputText = MutableStateFlow("")

    val uiState: StateFlow<ChatDetailUiState> =
        combine(chatStore.conversations, viewerRole, inputText) {
                conversations,
                role,
                currentInput,
            ->
                val currentUserId = mockCurrentUserId(role)
                val conversation =
                    currentUserId?.let { userId ->
                        conversations.firstOrNull {
                            it.chatId == chatId && it.containsUser(userId)
                        }
                }

                ChatDetailUiState(
                    messages =
                        currentUserId?.let { userId ->
                            conversation?.messages?.sortedBy { it.sentAt }?.map {
                                it.toMessageUiModel(userId)
                            }
                        } ?: emptyList(),
                    inputText = currentInput,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ChatDetailUiState(),
            )

    fun setViewerRole(role: UserRole) {
        viewerRole.value = role
        mockCurrentUserId(role)?.let { currentUserId ->
            chatStore.markAsRead(chatId, currentUserId)
        }
    }

    fun onTextChange(text: String) {
        if (text.length <= MAX_MESSAGE_LENGTH) {
            inputText.value = text
        }
    }

    fun sendMessage() {
        val currentUserId = mockCurrentUserId(viewerRole.value) ?: return
        if (inputText.value.isBlank()) return

        chatStore.sendMessage(
            chatId = chatId,
            senderId = currentUserId,
            text = inputText.value,
        )
        inputText.value = ""
    }
}
