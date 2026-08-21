package com.ahmetkaragunlu.guidemate.chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.chat.presentation.model.ChatListUiState
import com.ahmetkaragunlu.guidemate.chat.presentation.model.toChatUiModel
import com.ahmetkaragunlu.guidemate.chat.data.mock.ChatStore
import com.ahmetkaragunlu.guidemate.chat.data.mock.mockCurrentUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ChatListViewModel
@Inject
constructor(
    chatStore: ChatStore,
) : ViewModel() {
    private val viewerRole = MutableStateFlow<UserRole?>(null)

    val uiState: StateFlow<ChatListUiState> =
        combine(chatStore.conversations, viewerRole) { conversations, role ->
                val currentUserId = mockCurrentUserId(role) ?: return@combine ChatListUiState()
                val chats =
                    conversations
                        .asSequence()
                        .filter { it.containsUser(currentUserId) }
                        .sortedByDescending { it.lastActivityAt }
                        .mapNotNull { it.toChatUiModel(currentUserId) }
                        .toList()

                ChatListUiState(
                    chats = chats,
                    totalUnreadCount = chats.sumOf { it.unreadCount },
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ChatListUiState(),
            )

    fun setViewerRole(role: UserRole) {
        viewerRole.value = role
    }
}
