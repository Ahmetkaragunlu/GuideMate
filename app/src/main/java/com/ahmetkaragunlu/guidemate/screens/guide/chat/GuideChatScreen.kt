package com.ahmetkaragunlu.guidemate.screens.guide.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.screens.common.chat.screens.SharedChatListContent

@Composable
fun GuideChatScreen(
    viewModel: GuideChatViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit,
) {
    val chatList by viewModel.chatList.collectAsStateWithLifecycle()

    SharedChatListContent(
        chatList = chatList,
        onChatClick = onNavigateToDetail,
    )
}
