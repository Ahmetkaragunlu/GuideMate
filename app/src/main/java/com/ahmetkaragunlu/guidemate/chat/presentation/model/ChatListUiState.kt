package com.ahmetkaragunlu.guidemate.chat.presentation.model

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class ChatListUiState(
    val chats: List<ChatUiModel> = emptyList(),
    val totalUnreadCount: Int = 0,
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val errorMessage: String? = null,
    val userMessage: String? = null,
)
