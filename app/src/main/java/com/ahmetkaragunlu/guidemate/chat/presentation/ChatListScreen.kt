package com.ahmetkaragunlu.guidemate.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.chat.presentation.content.SharedChatListContent
import com.ahmetkaragunlu.guidemate.chat.presentation.model.ChatListUiState

@Composable
fun ChatListScreen(
    uiState: ChatListUiState,
    onNavigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SharedChatListContent(
        chatList = uiState.chats,
        onChatClick = onNavigateToDetail,
        modifier = modifier,
    )
}
