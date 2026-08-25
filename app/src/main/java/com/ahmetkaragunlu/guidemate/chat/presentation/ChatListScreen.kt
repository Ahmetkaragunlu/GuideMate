package com.ahmetkaragunlu.guidemate.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.chat.presentation.content.SharedChatListContent
import com.ahmetkaragunlu.guidemate.chat.presentation.model.ChatListUiState
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState

@Composable
fun ChatListScreen(
    uiState: ChatListUiState,
    onNavigateToDetail: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GuideMateContentState(
        state = uiState.loadState,
        onRetry = onRetry,
        modifier = modifier,
        errorMessage = uiState.errorMessage,
    ) {
        SharedChatListContent(
            chatList = uiState.chats,
            onChatClick = onNavigateToDetail,
            modifier = modifier,
        )
    }
}
