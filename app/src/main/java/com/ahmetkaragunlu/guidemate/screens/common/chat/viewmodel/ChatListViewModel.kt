package com.ahmetkaragunlu.guidemate.screens.common.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatListUiState
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.toChatUiModel
import com.ahmetkaragunlu.guidemate.screens.common.chat.store.ChatStore
import com.ahmetkaragunlu.guidemate.screens.common.chat.store.mockCurrentUserId
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
