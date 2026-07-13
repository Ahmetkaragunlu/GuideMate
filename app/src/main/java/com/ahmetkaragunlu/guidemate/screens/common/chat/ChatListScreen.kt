package com.ahmetkaragunlu.guidemate.screens.common.chat

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.screens.common.chat.content.SharedChatListContent
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatListUiState

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
